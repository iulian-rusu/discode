import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  public isAuthenticated: boolean | undefined;

  constructor(private readonly router: Router,
    private readonly userService: UserService) {
  }

  ngOnInit(): void {
    this.isAuthenticated = (this.userService.getUserId() !== null);
  }

  public logout(): void {
    this.userService.logoutUser();
    this.router.navigate(['auth']);
    location.reload();
  }
}
