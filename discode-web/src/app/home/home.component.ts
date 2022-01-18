import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ChatService } from '../shared/services/chat.service';
import { MessagesService } from '../shared/services/messages.service';
import { UserService } from '../shared/services/user.service';
import { Chat } from './models/chat.model';
import { Member } from './models/member.model';
import { Message } from './models/message.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  private subs: Subscription[];
  public createChatFormGroup: FormGroup;
  public userId: BigInteger;
  public chatList: Chat[] | undefined;

  public isChatSelected: boolean = false;
  public selectedChat: string | undefined;
  public chatMembers: Member[] | undefined;
  public chatId: BigInteger | undefined;
  public messagesPerPage: number = 30;
  display = 'none';
  messages: Message[] | undefined;

  constructor(
    private readonly userService: UserService,
    private readonly chatService: ChatService,
    private readonly messageService: MessagesService,
    private readonly formBuilder: FormBuilder
  ) {
    this.subs = new Array<Subscription>();
    this.userId = userService.getUserId();

    this.createChatFormGroup = this.formBuilder.group({
      chatName: ['', [Validators.required, Validators.minLength(2)]],
    });
  }

  ngOnInit(): void {
    this.getChats();

    this.messageService.onNewMember().subscribe(() => {
      // TODO
      // UPDATE MEMBER LIST ON THE RIGHT
      // REMOVE ADD BUTTON FOR THE NEW MEMBER
      // ADD MEMBER TO THE REMOVE LIST
      this.getMembers();
    });

    this.messageService.onNewChat().subscribe(() => {
      // TODO
      // ADD NEW CHAT TO LIST
      this.getChats();
    });
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
    this.getMessages();
    this.isChatSelected = true;
  }

  getChats(): void {
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

  getMessages() {
    this.subs.push(
      this.chatService
        .getMessages(this.chatId!!, 1, this.messagesPerPage)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.messages = data.body;
            this.messages?.reverse();
          }
        })
    );
  }

  openModal() {
    this.display = 'block';
  }
  onCloseHandled() {
    this.display = 'none';
    //this.createChatFormGroup.get("chatName")?.setValue("");
  }

  createChat(): void {
    this.subs.push(
      this.chatService
        .createChat(
          this.userId,
          this.createChatFormGroup.get('chatName')?.value
        )
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 201) {
            this.onCloseHandled();
            location.reload();
          }
        })
    );
  }

  leaveChat(chatId: BigInteger): void {
    this.subs.push(
      this.chatService.changeStatus(chatId, this.userId, 'LEFT').subscribe(
        (data: HttpResponse<any>) => {
          if (data.status == 200) {
            alert('You left the chat!');
            location.reload();
          }
        },
        () => {}
      )
    );
  }
}
