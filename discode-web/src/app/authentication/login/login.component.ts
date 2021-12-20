import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import {
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/login.service';
import { LoginModel } from '../models/login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, OnDestroy {
  public formGroup: FormGroup;
  private subs: Subscription[];

  constructor(
    private readonly router: Router,
    private readonly loginService: LoginService,
    private readonly formBuilder: FormBuilder
  ) {
    this.formGroup = this.formBuilder.group({
      username: '',
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(50),
        ],
      ],
    });

    this.subs = new Array<Subscription>();
  }

  ngOnInit(): void {}

  public login(): void {
    const data: LoginModel = this.formGroup.getRawValue();
    this.cleanErrors();
    this.subs.push(
      this.loginService.login(data).subscribe((data: HttpResponse<any>) => {
        if (data.status == 200) {
          sessionStorage.setItem('token', data.body['token']);
          sessionStorage.setItem('user', JSON.stringify(data.body['user']));
          this.router.navigate(['/home']);
        }
      }, this.handleError)
    );
  }

  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  handleError(responseError: HttpErrorResponse): void {
    let errorElement = document.createElement('div');
    errorElement.className = 'alert alert-danger';

    if (responseError.status == 409) {
      errorElement.innerHTML = 'Invalid username or password!';
    } else {
      errorElement.innerHTML = 'Something went wrong! Please try again.';
    }
    document.getElementById('errors')?.appendChild(errorElement);
  }

  cleanErrors(): void {
    let errorList = document.getElementById('errors')?.childNodes;
    errorList?.forEach((child) => {
      document.getElementById('errors')?.removeChild(child);
    });
  }
}
