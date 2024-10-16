import {Component} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {UserService} from "../explore/user-list/services/user.service";
import {RegisterDto} from "../../shared/Dtos/register.dto";
import {switchMap} from "rxjs";
import {LoginDto} from "../../shared/Dtos/login.dto";
import {AuthService} from "../../shared/services/authService/auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [
        ReactiveFormsModule,
        NgIf,
        NgClass
    ],
    templateUrl: './register.component.html',
    styleUrl: './register.component.scss'
})
export class RegisterComponent
{
    registerForm: FormGroup

    private readonly userService: UserService;

    private readonly authService: AuthService;

    private readonly router: Router;

    constructor(userService: UserService,
                authService: AuthService,
                router: Router)
    {
        this.userService = userService;
        this.authService = authService;
        this.router = router;

        this.registerForm = new FormGroup({
            username: new FormControl(
                "", [Validators.required, Validators.minLength(4), Validators.maxLength(20)]
            ),
            email: new FormControl(
                "", [Validators.required, Validators.email]
            ),
            password: new FormControl(
                "", [Validators.required, Validators.minLength(8), Validators.maxLength(20)]
            ),
            confirmPassword: new FormControl(
                "", [Validators.required]
            ),
        });
    }

    handleSubmit()
    {
        if (this.registerForm.invalid)
        {
            return;
        }

        if (this.confirmPassword.value !== this.password.value)
        {
            this.confirmPassword.setValue("");
            this.confirmPassword.setErrors({mismatch: true});
            return;
        }

        const toRegister: RegisterDto = {
            username: this.username.value,
            password: this.password.value,
            email: this.email.value
        }

        this.userService.createUser(toRegister)
            .pipe(
                switchMap(
                    () =>
                    {
                        const toLogin: LoginDto = {
                            username: this.username.value,
                            password: this.password.value
                        }

                        return this.authService.login(toLogin);
                    }
                )
            )
            .subscribe({
                error: (error) =>
                {
                    switch (error.status)
                    {
                        case 409:
                            this.username.setErrors({conflict: true});
                            break;
                    }
                },
                complete: () =>
                {
                    void this.router.navigateByUrl("/");
                }
            });
    }

    get username()
    {
        return this.registerForm.get("username")!;
    }

    get email()
    {
        return this.registerForm.get("email")!;
    }

    get password()
    {
        return this.registerForm.get("password")!;
    }

    get confirmPassword()
    {
        return this.registerForm.get("confirmPassword")!;
    }

}
