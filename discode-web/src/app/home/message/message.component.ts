import { DatePipe } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/shared/services/chat.service';
import { CodeService } from 'src/app/shared/services/code.service';
import { UserService } from 'src/app/shared/services/user.service';
import { Message } from '../models/message.model';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
  providers: [DatePipe],
})
export class MessageComponent implements OnInit, OnDestroy {
  @Input()
  public message!: Message;

  @Input()
  public languages!: any[];

  code: string | undefined;

  display = 'none';
  spinnerDisplay = 'none';
  reason: string | undefined;
  subs: Subscription[];
  userId: BigInteger;
  mode = '';

  constructor(
    private readonly chatService: ChatService,
    private readonly codeService: CodeService,
    private readonly userService: UserService,
    private readonly datePipe: DatePipe
  ) {
    this.subs = new Array<Subscription>();
    this.userId = userService.getUserId();
  }

  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  ngOnInit(): void {}

  isCode(): boolean {
    if (
      this.message?.content?.startsWith('`') &&
      this.message?.content?.endsWith('`')
    ) {
      this.getCode();
      return true;
    }
    return false;
  }

  getCode() {
    this.code = this.message.content?.substring(1, this.message.content?.length - 1);
  }

  onChange(newMode: string) {
    this.mode = newMode;
}

  transform(date: Date): string | null {
    let pipe = new DatePipe('en-US');
    let formattedDate = pipe.transform(date, 'short');
    return formattedDate;
  }

  reportMessage() {
    this.display = 'none';
    this.subs.push(
      this.chatService
        .reportMessage(this.message?.messageId!!, this.userId, this.reason)
        .subscribe(
          (data: HttpResponse<any>) => {
            if (data.status == 201) {
              alert('Message was reported!');
            }
          },
          () => {
            alert('Message was already reported!');
          }
        )
    );
  }

  openModal() {
    this.display = 'block';
  }
  onCloseHandled() {
    this.display = 'none';
  }

  runCode(){
    this.spinnerDisplay = 'block';
    this.subs.push(
      this.codeService
        .runCode(this.message.messageId!, this.mode)
        .subscribe(
          (data: HttpResponse<any>) => {
            if (data.status == 200) {
              this.spinnerDisplay = 'none';
              this.message.codeOutput = data.body["codeOutput"];
            }
          },
        )
    );
  }
}
