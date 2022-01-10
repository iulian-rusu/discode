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
const host = process.env.DISCODE_CHAT_HOST || "localhost";
const port = 8010;

const httpServer = createServer();
const server = new Server(httpServer, {
    cors: {
        origin: "*",
        methods: ["GET", "POST"]
    }
});

// { chatId: "", memberCount: 0 }
let rooms = [];

server.on("connection", socket => {
    console.info(`Client connecting with id=${socket.id}`);
    let userId = undefined;
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

    socket.on("connect-user", pUserId => {
        userId = pUserId;
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
                        "userId": userId,
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

        /*
         * userid, content, token
         * când e gata primești răspuns și trimiți la toți
         * // socket.emit("invalid-chat"); dacă eroare
         */
});

httpServer.listen(port, host);
