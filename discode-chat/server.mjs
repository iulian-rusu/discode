import fetch from "node-fetch";
import { Server } from "socket.io";
/*
 * Treated events:
 *
 * connect-user
 * join
 * leave
 * message
*/

/*
 * Sent events:
 *
 * message
 * exception
*/


const backend_url = `http://localhost:8008/api`;
const port = 8010;
const server = new Server(port, {
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
        disconnect();
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
            for (let con in connection) {
                if (con.userId == newUser) {
                    con.socket.emit("new-chat");
                    break;
                }
            }
            socket.emit("new-member");
            socket.broadcast.to(room).emit("new-member");
        }
    });
});
