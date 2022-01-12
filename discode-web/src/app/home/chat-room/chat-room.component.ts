import { HttpResponse } from '@angular/common/http';
import {
  AfterViewChecked,
  Component,
  ElementRef,
  HostListener,
  Input,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CodemirrorComponent } from '@ctrl/ngx-codemirror';
import * as CodeMirror from 'codemirror';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/shared/services/chat.service';
import { CodeService } from 'src/app/shared/services/code.service';
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

  @ViewChild('scrollMessages') private scrollContainer!: ElementRef;
  @ViewChild('code')
  private codeEditor!: CodemirrorComponent;

  subs: Subscription[];
  deleteMemberModalDisplay = 'none';
  addMemberModalDisplay = 'none';
  spinnerDisplay = 'none';
  codeModalDisplay = 'none';
  searchText = '';
  mode = "text/x-c++src";
  search: Member[] | undefined;
  messageFormGroup: FormGroup;
  codeMessage = '';
  languages: any[] | undefined;

  constructor(
    private readonly chatService: ChatService,
    private readonly userService: UserService,
    private readonly messageService: MessagesService,
    private readonly formBuilder: FormBuilder,
    private readonly codeService: CodeService,
    private readonly router: Router
  ) {
    this.messageService.connect();
    this.subs = new Array<Subscription>();

    this.messageFormGroup = this.formBuilder.group({
      message: [
        '',
        [
          Validators.required,
          Validators.maxLength(1000),
          Validators.minLength(1),
        ],
      ],
    });
    this.scrollToBottom();
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
    this.getLanguages();
    this.messageService.onNewMessage().subscribe((msg) => {
      let m: Message = msg as any;
      this.messages?.push(m);
      this.scrollToBottom();
    });
  }

  getLanguages() {
    this.subs.push(
      this.codeService
        .getLanguages()
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.languages = data.body;
          }
        })
    );
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.chatId) {
      this.messageService.joinChat(changes.chatId.currentValue);
      this.scrollToBottom();
    }
  }

  scrollToBottom(): void {
    try {
      this.scrollContainer.nativeElement.scrollTop =
        this.scrollContainer.nativeElement.scrollHeight;
    } catch (err) {}
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

  onCloseHandled(){
    this.codeModalDisplay = 'none';
  }

  onSendCode() {
    if(this.codeMessage !== '') {
        const source = "`" + this.codeMessage + "`";
        this.codeMessage = "";
        this.messageService.sendMessage(source);
    }
    this.codeEditor.codeMirror?.setValue(this.codeMessage);
    this.codeEditor.codeMirror?.refresh();
    this.onCloseHandled();
  }

  openCodeModal(){
    this.codeModalDisplay = 'block';
    this.codeEditor.codeMirror?.focus();
  }

  onChange(newMode: string) {
    this.mode = newMode;
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
              this.messageService.newMember();
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

  sendMessage() {
    this.messageService.sendMessage(
      this.messageFormGroup.get('message')?.value
    );
    this.messageFormGroup.get('message')?.reset();
  }

  /*
  @HostListener('scroll', ['$event'])
  onScroll(event: any) {
    // visible height + pixel scrolled >= total height
    //console.log(event.target.offsetHeight + " " + event.target.scrollTop);
    if (
      event.target.offsetHeight + event.target.scrollTop >=
      event.target.scrollHeight
    ) {
      console.log('End');
    }
  }
  */

}
