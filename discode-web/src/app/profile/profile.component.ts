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
import { DomSanitizer } from '@angular/platform-browser';
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
  base64textString: string | undefined;
  imagePath: any;
  image: any;

  constructor(
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly formBuilder: FormBuilder,
    private sanitizer: DomSanitizer
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

    this.subs = new Array<Subscription>();
    this.userId = userService.getUserId();
  }

  ngOnDestroy(): void {
    this.subs.forEach((sub) => {
      sub.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.subs.push(
      this.userService
        .getUser(this.userId)
        .subscribe((data: HttpResponse<any>) => {
          if (data.status == 200) {
            this.profileFormGroup
              .get('firstName')
              ?.setValue(data.body['firstName']);

            this.profileFormGroup
              .get('lastName')
              ?.setValue(data.body['lastName']);

            this.profileFormGroup.get('email')?.setValue(data.body['email']);

            this.profileFormGroup
              .get('description')
              ?.setValue(data.body['description']);

            this.imagePath = data.body['imagePath'];

            if (this.imagePath) {
              this.subs.push(
                this.userService
                  .getProfileImage(this.imagePath)
                  .subscribe((data: any) => {
                    let unsafeImageUrl = URL.createObjectURL(data);
                    this.image =
                      this.sanitizer.bypassSecurityTrustUrl(unsafeImageUrl);
                  })
              );
            }
          }
        })
    );
  }

  reloadComponent() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([this.router.url]);
  }

  updateProfile(): void {
    const data: User = this.profileFormGroup.getRawValue();
    this.cleanErrors('message-profile');
    this.subs.push(
      this.userService.updateUser(this.userId, data).subscribe(
        (data: HttpResponse<any>) => {
          if (data.status == 200) {
            alert('Profile updated successfully!');
            this.reloadComponent();
          } else {
            this.handleError('message-profile');
          }
        },
        (err) => {
          this.handleError('message-profile');
        }
      )
    );
  }

  updatePassword(): void {
    const data: User = this.passwordFormGroup.getRawValue();
    this.cleanErrors('message-password');
    this.subs.push(
      this.userService.updateUser(this.userId, data).subscribe(
        (data: HttpResponse<any>) => {
          if (data.status == 200) {
            alert('Password changed!');
            this.reloadComponent();
          } else {
            this.handleError('message-password');
          }
        },
        (err) => {
          this.handleError('message-password');
        }
      )
    );
  }

  updateImage(fileInput: any): void {
    let files: File[] = fileInput.files;
    var file = files[0];
    this.cleanErrors('message-image');

    if (files.length < 1) {
      return;
    }

    var reader = new FileReader();
    reader.onload = this.handleReaderLoaded.bind(this);
    reader.readAsBinaryString(file);
  }

  handleReaderLoaded(readerEvt: any) {
    var binaryString = readerEvt.target.result;
    const data: User = new User();
    data.image = btoa(binaryString);

    this.subs.push(
      this.userService.updateUser(this.userId, data).subscribe(
        (data: HttpResponse<any>) => {
          if (data.status == 200) {
            alert('Profile picture changed!');
            this.reloadComponent();
          } else {
            this.handleError('message-image');
          }
        },
        (err) => {
          this.handleError('message-image');
        }
      )
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

  handleError(
    elementId: string,
    message: string = 'Something went wrong! Please try again.'
  ) {
    let errorElement = document.createElement('div');
    errorElement.className = 'alert alert-danger';
    errorElement.innerHTML = message;
    document.getElementById(elementId)?.appendChild(errorElement);
  }

  cleanErrors(errors: string): void {
    let errorList = document.getElementById(errors)?.childNodes;
    errorList?.forEach((child) => {
      document.getElementById(errors)?.removeChild(child);
    });
  }
}
