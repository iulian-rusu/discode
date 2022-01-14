import { Component } from '@angular/core';
import { UserService } from './shared/services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'Discode';
  public isAuthenticated: boolean | undefined;

  constructor(public readonly userService: UserService) {
    this.isAuthenticated = userService.getUserId() !== null;
  }

  getNotification(evt: boolean) {
    this.isAuthenticated = evt;
  }

  componentAdded(evt: any){
    this.isAuthenticated = this.userService.getUserId() !== null;
  }
}
