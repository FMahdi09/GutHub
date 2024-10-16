import {Component} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";

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

    constructor()
    {
        this.registerForm = new FormGroup({
            username: new FormControl(
                "",
                [
                    Validators.required,
                    Validators.minLength(4),
                    Validators.maxLength(20),
                ]
            ),
            email: new FormControl(
                "",
                [
                    Validators.required,
                    Validators.email
                ]
            ),
            password: new FormControl(
                "",
                [
                    Validators.required,
                    Validators.minLength(8),
                    Validators.maxLength(20),
                ]
            ),
            confirmPassword: new FormControl(
                "",
                []
            ),
        });
    }

    handleSubmit()
    {
        if (!this.registerForm.valid)
        {
            console.log("invalid")
            return;
        }
    }
}
