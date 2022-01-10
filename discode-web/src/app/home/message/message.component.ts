import { DatePipe } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/shared/services/chat.service';
import { Message } from '../models/message.model';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
  providers: [DatePipe],
})
export class MessageComponent implements OnInit, OnDestroy {
  @Input() public message: Message | undefined;

  display = 'none';
  reason: string | undefined;
  subs: Subscription[];
  userId:BigInteger = JSON.parse(sessionStorage.getItem('user')!)['userId'];

  constructor(
    private readonly chatService: ChatService,
    private readonly datePipe: DatePipe
  ) {
    this.subs = new Array<Subscription>();
  }

  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  ngOnInit(): void {}

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
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 201) {
            alert('Message was reported!');
          }
        }, () =>{
          alert("Message was already reported!");
        } )
    );
  }

  openModal() {
    this.display = 'block';
  }
  onCloseHandled() {
    this.display = 'none';
  }
}
