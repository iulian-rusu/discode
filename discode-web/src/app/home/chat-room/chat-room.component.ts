import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/shared/services/chat.service';
import { UserService } from 'src/app/shared/services/user.service';
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
  deleteMemberModalDisplay = 'none';
  addMemberModalDisplay = 'none';
  spinnerDisplay = 'none';
  searchText = '';

  search: Member[] | undefined;

  constructor(
    private readonly chatService: ChatService,
    private readonly userService: UserService,
    private readonly router: Router
  ) {
    this.subs = new Array<Subscription>();
  }
  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  ngOnInit(): void {}

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
        .changeStatus(this.chatId!!, userId, "LEFT")
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            alert(username + ' has been removed from chat!');
          }
        })
    );
  }

  addInChat(userId: BigInteger, username: string): void {
    if(this.isAlreadyMember(userId) === "LEFT"){
      this.subs.push(
        this.chatService
          .changeStatus(this.chatId!!, userId, "GUEST")
          .subscribe((data: HttpResponse<any>) => {
            if (data.status == 200) {
              alert(username + ' joined the chat!');
              this.searchText = '';
            }
          })
      );
    }
    else{
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
}
