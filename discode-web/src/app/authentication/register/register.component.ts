import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { RegisterModel } from '../models/register.model';
import { RegisterService } from '../services/register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  public formGroup: FormGroup;
  private subs: Subscription[];

  constructor(
    private readonly router: Router,
    private readonly registerService: RegisterService,
    private readonly formBuilder: FormBuilder
  ) {
    this.formGroup = this.formBuilder.group({
      firstName: [
        '',
        [
          Validators.required,
          Validators.maxLength(50),
          Validators.minLength(2),
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.maxLength(150),
          Validators.minLength(2),
        ],
      ],
      username: '',
      email: ['', [Validators.required, Validators.email]],
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

  public register(): void {
    console.log(this.formGroup.getRawValue());
    const data: RegisterModel = this.formGroup.getRawValue();
    this.subs.push(
      this.registerService
        .register(data)
        .subscribe((data: HttpResponse<any>) => {
          console.log("hjsdjd");
          if (data.status == 200) {
            this.router.navigate(['/home']);
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
