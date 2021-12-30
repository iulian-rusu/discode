import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from '../shared/services/user.service';
import { Chat } from './models/chat.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  private subs: Subscription[];
  private userId: string;
  public chatList: Chat[] | undefined;

  constructor(private readonly userService: UserService) {
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
}
