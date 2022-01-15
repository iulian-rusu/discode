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
    this.connect();
  }

  connect() {
    let id = this.userService.getUserId();
    this.socket.emit('connect-user', id);
  }

  onNewMessage() {
    return new Observable((observer) => {
      this.socket.on('message', (msg: Message) => {
        console.log('new-message');
        observer.next(msg);
      });
    });
  }

  onNewMember() {
    return new Observable((observer) => {
      this.socket.on('new-member', () => {
        console.log('new-member');
        observer.next();
      });
    });
  }

  onNewChat() {
    return new Observable((observer) => {
      this.socket.on('new-chat', () => {
        console.log('new-chat');
        observer.next();
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
  }
}
