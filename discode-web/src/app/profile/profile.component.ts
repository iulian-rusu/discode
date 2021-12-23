import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from '../shared/models/user.model';
import { UserService } from '../shared/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit, OnDestroy {
  private subs: Subscription[];
  private userId: string;
  public profileFormGroup: FormGroup;
  public passwordFormGroup: FormGroup;
  public imageFormGroup: FormGroup;

  constructor(
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly formBuilder: FormBuilder
  ) {
    this.profileFormGroup = this.formBuilder.group({
      firstName: [
        '',
        [
          Validators.required,
          Validators.maxLength(50),
          Validators.minLength(2),
          Validators.pattern('^[A-Z]([- ]?[a-zA-Z]+)+$'),
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.maxLength(150),
          Validators.minLength(2),
          Validators.pattern('^[A-Z]([- ]?[a-zA-Z]+)+$'),
        ],
      ],
      email: ['', [Validators.required, Validators.email]],
      description: '',
    });

    this.passwordFormGroup = this.formBuilder.group(
      {
        password: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(50),
          ],
        ],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.checkPasswords }
    );

    this.imageFormGroup = this.formBuilder.group({
      image: ['', Validators.required],
    });

    this.subs = new Array<Subscription>();
    this.userId = JSON.parse(sessionStorage.getItem('user')!)['userId'];
  }

  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  ngOnInit(): void {
    //this.userId = JSON.parse(sessionStorage.getItem('user')!)['id'];
    this.subs.push(
      this.userService.getUser(this.userId).subscribe((data: HttpResponse<any>) => {
        if (data.status == 200) {
          console.log(data.body);
          this.profileFormGroup.get("firstName")?.setValue(data.body["firstName"]);
          this.profileFormGroup.get("lastName")?.setValue(data.body["lastName"]);
          this.profileFormGroup.get("email")?.setValue(data.body["email"]);
          this.profileFormGroup.get("description")?.setValue(data.body["description"]);
        }
      })
    );
    
  }

  updateImage(): void {}

  updateProfile(): void {
    const data: User = this.profileFormGroup.getRawValue();
    this.cleanErrors("errors-profile");
    this.subs.push(
      this.userService
        .updateUser(this.userId, data)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.router.navigate([this.router.url]);
          }
        }, this.handleErrorUpdateProfile)
    );
  }

  updatePassword(): void {
    const data: User = this.passwordFormGroup.getRawValue();
    this.cleanErrors("errors-password");
    this.subs.push(
      this.userService
        .updateUser(this.userId, data)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.router.navigate([this.router.url]);
          }
        }, this.handleErrorUpdatePassword)
    );
  }

  checkPasswords: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let pass = group.get('password')!!.value;
    let confirmPass = group.get('confirmPassword')!!.value;
    return pass == confirmPass ? null : { notSame: true };
  };

  public isInvalid(form: AbstractControl): boolean {
    return form.invalid && form.dirty;
  }

  handleErrorUpdateProfile(responseError: HttpErrorResponse): void {
    let errorElement = document.createElement('div');
    errorElement.className = 'alert alert-danger';

    errorElement.innerHTML = 'Something went wrong! Please try again.';
    document.getElementById('errors')?.appendChild(errorElement);
  }

  handleErrorUpdatePassword(responseError: HttpErrorResponse): void {
    let errorElement = document.createElement('div');
    errorElement.className = 'alert alert-danger';

    errorElement.innerHTML = 'Something went wrong! Please try again.';
    document.getElementById('errors')?.appendChild(errorElement);
  }

  cleanErrors(errors: string): void {
    let errorList = document.getElementById(errors)?.childNodes;
    errorList?.forEach((child) => {
      document.getElementById(errors)?.removeChild(child);
    });
  }
}
