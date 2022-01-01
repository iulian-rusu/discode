import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChatService } from '../shared/services/chat.service';
import { UserService } from '../shared/services/user.service';
import { Chat } from './models/chat.model';
import { Member } from './models/member.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  private subs: Subscription[];
  private userId: string;
  public chatList: Chat[] | undefined;

  public isChatSelected: boolean = false;
  public selectedChat: string | undefined;
  public chatMembers: Member[] | undefined;
  public chatId: BigInteger | undefined;

  constructor(
    private readonly userService: UserService,
    private readonly chatService: ChatService
  ) {
    this.subs = new Array<Subscription>();
    this.userId = JSON.parse(sessionStorage.getItem('user')!)['userId'];
  }

  ngOnInit(): void {
    this.subs.push(
      this.userService
        .getChats(this.userId)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.chatList = data.body;
          }
        })
    );
  }

  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  chatClick(
    chatId: BigInteger | undefined,
    chatName: string | undefined
  ): void {
    this.selectedChat = chatName;
    this.chatId = chatId;
    this.getMembers();
    this.isChatSelected = true;
  }

  getMembers(): void {
    this.subs.push(
      this.chatService
        .getChatMembers(this.chatId!!)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.chatMembers = data.body;
          }
        })
    );
  }
}
