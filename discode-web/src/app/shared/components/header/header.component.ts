import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  
  @Input() public isAuthenticated: boolean | undefined;

  @Output() notifyParent: EventEmitter<any> = new EventEmitter();

  constructor(private readonly router: Router,
    private readonly userService: UserService) {
  }

  public logout(): void {
    this.userService.logoutUser();
    this.router.navigate(['auth']);
    this.notifyParent.emit(false);
  }
}
