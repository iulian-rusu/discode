import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from '../shared/service/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit, OnDestroy {
  private subs: Subscription[];
  constructor(private readonly userService: UserService) {
    this.subs = new Array<Subscription>();
  }
  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.subs.push(
      this.userService.getUsers().subscribe((data: HttpResponse<any>) => {
        if (data.status == 200) {
          console.log(data.body);
        }
      })
    );
  }
}
