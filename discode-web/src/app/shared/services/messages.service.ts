import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { io, Socket } from 'socket.io-client';
import { Message } from 'src/app/home/models/message.model';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {
  private socket: Socket;

  constructor(private readonly userService: UserService) {
    const host = 'localhost';
    const port = 8010;
    this.socket = io(`http://${host}:${port}`);
  }

  connect() {
    let id = this.userService.getUserId();
    this.socket.emit('connect-user', id);
  }

  onNewMessage() {
    return new Observable((observer) => {
      this.socket.on('message', (msg: Message) => {
        observer.next(msg);
      });
    });
  }

  onNewMember() {
    return new Observable((observer) => {
      this.socket.on('new-chat', (msg: any) => {
        console.log("abc");
        observer.next(msg);
      });
    });
  }

  joinChat(chatId: BigInteger) {
    this.socket.emit('join', chatId);
  }

  leave() {
    this.socket.emit('leave');
  }

  sendMessage(message: String) {
    let token = this.userService.getUserToken();
    this.socket.emit('message', message, token);
  }

  newMember(userId: BigInteger) {
    this.socket.emit('new-member', userId);
    console.log("add user");
  }
}
