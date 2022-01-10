import { HttpResponse } from '@angular/common/http';
import {
  AfterViewChecked,
  Component,
  ElementRef,
  Input,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/shared/services/chat.service';
import { MessagesService } from 'src/app/shared/services/messages.service';
import { UserService } from 'src/app/shared/services/user.service';
import { Member } from '../models/member.model';
import { Message } from '../models/message.model';

@Component({
  selector: 'app-chat-room',
  templateUrl: './chat-room.component.html',
  styleUrls: ['./chat-room.component.scss'],
})
export class ChatRoomComponent implements OnInit, OnDestroy, AfterViewChecked {
  @Input() public chatId: BigInteger | undefined;
  @Input() public chatName: string | undefined;
  @Input() public chatMembers: Member[] | undefined;
  @Input() public messages: Message[] | undefined;

  @ViewChild('scrollMe') private myScrollContainer!: ElementRef;

  subs: Subscription[];
  deleteMemberModalDisplay = 'none';
  addMemberModalDisplay = 'none';
  spinnerDisplay = 'none';
  searchText = '';
  search: Member[] | undefined;
  messageFormGroup: FormGroup;

  constructor(
    private readonly chatService: ChatService,
    private readonly userService: UserService,
    private readonly messageService: MessagesService,
    private readonly formBuilder: FormBuilder,
    private readonly router: Router
  ) {
    this.subs = new Array<Subscription>();

    this.messageFormGroup = this.formBuilder.group(
      {
        message: [
          '',
          [
            Validators.required,
            Validators.maxLength(1000),
            Validators.minLength(1),
          ]]
      }
    );

  }
  ngAfterViewChecked(): void {
    this.scrollToBottom(); 
  }
  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });

    this.messageService.leave();
  }

  ngOnInit(): void {
    this.messageService.connect();

    this.messageService.onNewMessage().subscribe(msg => {
      let m: Message = msg as any;
      this.messages?.push(m);

      this.scrollToBottom();
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.chatId) {
      this.messageService.joinChat(changes.chatId.currentValue);
    }
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    
    try {
        this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
        console.log("scroll");
    } catch(err) {
      console.log(err);
     }   
                   
}

  openDeleteMemberModal() {
    this.deleteMemberModalDisplay = 'block';
  }

  onDeleteMemberModalCloseHandled() {
    this.deleteMemberModalDisplay = 'none';
  }

  openAddMemberModal() {
    this.addMemberModalDisplay = 'block';
  }

  onAddMemberModalCloseHandled() {
    this.addMemberModalDisplay = 'none';
  }

  deleteChat(): void {
    this.subs.push(
      this.chatService
        .deleteChat(this.chatId!!)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            location.reload();
            //this.router.navigate(["/home"]);
          }
        })
    );
  }

  removeFromChat(userId: BigInteger, username: string): void {
    this.subs.push(
      this.chatService
        .changeStatus(this.chatId!!, userId, 'LEFT')
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            alert(username + ' has been removed from chat!');
          }
        })
    );
  }

  addInChat(userId: BigInteger, username: string): void {
    if (this.isAlreadyMember(userId) === 'LEFT') {
      this.subs.push(
        this.chatService
          .changeStatus(this.chatId!!, userId, 'GUEST')
          .subscribe((data: HttpResponse<any>) => {
            if (data.status == 200) {
              alert(username + ' joined the chat!');
              this.searchText = '';
            }
          })
      );
    } else {
      this.subs.push(
        this.chatService
          .addMemberToChat(this.chatId!!, userId)
          .subscribe((data: HttpResponse<any>) => {
            if (data.status == 200) {
              alert(username + ' joined the chat!');
              this.searchText = '';
            }
          })
      );
    }
  }

  getUsers(): void {
    if (this.searchText == '') {
      this.search = undefined;
      return;
    }

    this.spinnerDisplay = 'block';
    this.subs.push(
      this.userService
        .getUsers(this.searchText)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.search = data.body;
            this.spinnerDisplay = 'none';
          }
        })
    );
  }

  isAlreadyMember(userId: BigInteger): string {
    for (let member of this.chatMembers!!) {
      if (member.userId === userId) {
        return member.status!!;
      }
    }
    return 'NONE';
  }

  sendMessage(){
    this.messageService.sendMessage(this.messageFormGroup.get('message')?.value);
    this.messageFormGroup.get('message')?.reset();
  }
}
