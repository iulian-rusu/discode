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
import { UserService } from 'src/app/shared/services/user.service';
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
        private readonly formBuilder: FormBuilder,
        private readonly userService: UserService
    ) {
        this.formGroup = this.formBuilder.group(
            {
                firstName: [
                    '',
                    [
                        Validators.required,
                        Validators.maxLength(64),
                        Validators.minLength(2),
                        Validators.pattern('^[A-Z]([- ]?[a-zA-Z]+)+$'),
                    ],
                ],
                lastName: [
                    '',
                    [
                        Validators.required,
                        Validators.maxLength(64),
                        Validators.minLength(2),
                        Validators.pattern('^[A-Z]([- ]?[a-zA-Z]+)+$'),
                    ],
                ],
                username: [
                    '',
                    [
                        Validators.required,
                        Validators.maxLength(64),
                        Validators.minLength(3),
                        Validators.pattern('^[_a-zA-Z]\\w{2,}$'),
                    ],
                ],
                email: ['',
                    [
                        Validators.required,
                        Validators.maxLength(128),
                        Validators.email
                    ]
                ],
                password: [
                    '',
                    [
                        Validators.required,
                        Validators.minLength(8),
                        Validators.maxLength(64),
                    ],
                ],
                confirmPassword: ['', Validators.required],
            },
            { validators: this.checkPasswords }
        );

        this.subs = new Array<Subscription>();
    }

    ngOnInit(): void { }

    checkPasswords: ValidatorFn = (
        group: AbstractControl
    ): ValidationErrors | null => {
        let pass = group.get('password')!!.value;
        let confirmPass = group.get('confirmPassword')!!.value;
        return pass == confirmPass ? null : { notSame: true };
    };

    public register(): void {
        const data: RegisterModel = this.formGroup.getRawValue();
        this.cleanErrors();
        this.subs.push(
            this.registerService
                .register(data)
                .subscribe((data: HttpResponse<any>) => {
                    if (data.status == 201) {
                        this.userService.setUser(data.body['token'], JSON.stringify(data.body['user']));
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

    public isInvalid(form: AbstractControl): boolean {
        return form.invalid && form.dirty;
    }

    handleError(responseError: HttpErrorResponse): void {
        let errorElement = document.createElement('div');
        errorElement.className = 'alert alert-danger';

        errorElement.innerHTML = 'Something went wrong! Please try again.';
        document.getElementById('errors')?.appendChild(errorElement);
    }

    cleanErrors(): void {
        let errorList = document.getElementById('errors')?.childNodes;
        errorList?.forEach((child) => {
            document.getElementById('errors')?.removeChild(child);
        });
    }
}
