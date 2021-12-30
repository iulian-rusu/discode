import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  public isAuthenticated: boolean | undefined;

  constructor(private readonly router: Router) {
  }

  ngOnInit(): void {
    this.isAuthenticated = (JSON.parse(sessionStorage.getItem('user')!) !== null);
    console.log(JSON.parse(sessionStorage.getItem('user')!) !== null);
  }

  public logout(): void {
    sessionStorage.clear();
    this.ngOnInit();
    this.router.navigate(['auth']);
  }
}
