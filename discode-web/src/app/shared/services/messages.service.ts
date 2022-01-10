import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { io, Socket } from 'socket.io-client';
import { Message } from 'src/app/home/models/message.model';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {
  private socket: Socket;

  constructor() {
    const host = 'localhost';
    const port = 8010;
    this.socket = io(`http://${host}:${port}`);
  }

  connect() {
    let id = JSON.parse(sessionStorage.getItem('user')!)['userId'];
    this.socket.emit('connect-user', id);
  }

  onNewMessage() {
    return new Observable((observer) => {
      this.socket.on('message', (msg: Message) => {
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
    let token = sessionStorage.getItem('token');
    this.socket.emit('message', message, token);
  }

  newMember() {
    this.socket.emit('new-member');
  }
}
