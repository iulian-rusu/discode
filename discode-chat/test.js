const io = require("socket.io-client");
const host = "localhost";
const port = 8010;
const socket = io.connect(`http://${host}:${port}`);

const test_token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlzQWRtaW4iOmZhbHNlfQ.LqX0RlhLztbuA4Fs2XQnSjuhfxdjLaQKZXp5R-KNAL4";
const userId = 1;
const chatId1 = 1;
const chatId2 = 2;

socket.on("message", (body) => {
    console.log(`Received ${body.content} from ${body.author.chatMemberId} in ${body.author.chatId}`);
});

socket.on("exception", () => {
    console.log("An error occured.");
    // Action: Log out? and print an error occured
});

socket.emit("connect-user", userId);
socket.emit("join", chatId1);
socket.emit("message", "Wow, a message", test_token);
socket.emit("join", chatId2);
socket.emit("message", "Wow, another message", test_token);
socket.emit("leave");
