import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/shared/services/chat.service';
import { Member } from '../models/member.model';

@Component({
  selector: 'app-chat-room',
  templateUrl: './chat-room.component.html',
  styleUrls: ['./chat-room.component.scss'],
})
export class ChatRoomComponent implements OnInit, OnDestroy {
  @Input() public chatId: BigInteger | undefined;
  @Input() public chatName: string | undefined;
  @Input() public chatMembers: Member[] | undefined;
  subs: Subscription[];

  constructor(private readonly chatService: ChatService,
    private readonly router: Router) {
    this.subs = new Array<Subscription>();
  }
  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  ngOnInit(): void {}

  addMember(): void {}

  removeMember(): void {}

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
}
