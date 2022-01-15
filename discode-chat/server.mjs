import fetch from "node-fetch";
import { createServer } from "http";
import { Server } from "socket.io";
import process from 'process';

/*
 * Treated events:
 *
 * connect-user
 * join
 * leave
 * message
 * new-member
*/

/*
 * Sent events:
 *
 * message
 * exception
 * new-chat
 * new-member
*/

const backend_host = process.env.DISCODE_BACKEND_HOST || "localhost";
const backend_url = `http://${backend_host}:8008/api`;
const port = 8010;

const httpServer = createServer();
const server = new Server(httpServer, {
    cors: {
        origin: "*",
        methods: ["GET", "POST"]
    }
});

// { userId: 0, socket: {} }
let connections = [];
// { chatId: 0, memberCount: 0 }
let rooms = [];

server.on("connection", socket => {
    console.info(`Client connecting with id=${socket.id}`);
    let connection = undefined;
    let room = undefined;

    let disconnect = () => {
        leave();
        if(connection)
            connections.splice(connections.indexOf(connection), 1);
    };

    let leave = () => {
        if (room) {
            --room.memberCount;
            if (room.memberCount <= 0)
                rooms.splice(rooms.indexOf(room), 1);

            socket.leave(room);
        }

        room = undefined;
    };

    socket.on("connect-user", userId => {
        connection = { userId: userId, socket: socket };
        connections.push(connection);
    });

    socket.on("disconnect", () => {
        console.info(`Client disconnecting with id=${socket.id}`);
        disconnect();
    });

    socket.on("join", chatId => {
        if (room)
            disconnect();
        let temp = rooms.filter((element) => element.chatId == chatId);
        room = temp.length > 0 ? temp[0] : undefined;
        if (!room) {
            room = { chatId: chatId, memberCount: 1 };
            rooms.push(room);
        }
        socket.join(room);
    });

    socket.on("leave", () => {
        leave();
    });

    socket.on("message", async (content, token) => {
        if (room) {
            fetch(`${backend_url}/chats/${room.chatId}/messages`, {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                        "userId": connection.userId,
                        "content": content
                })
            })
            .then(response => response.json())
            .then(body => {
                if (body.status) {
                    socket.emit("exception");
                }
                else {
                    socket.emit("message", body);
                    socket.broadcast.to(room).emit("message", body);
                }
            })
            .catch(exception => {
                socket.emit("exception");
            });
        }
    });

    socket.on("new-member", newUser => {
        if (room) {
            socket.emit("new-member");
            socket.broadcast.to(room).emit("new-member");
            for (let con of connections) {
                if (con.userId == newUser) {
                    con.socket.emit("new-chat");
                    break;
                }
            }
        }
    });
});

httpServer.listen(port);
