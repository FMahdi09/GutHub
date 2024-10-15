import {Component} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [
        ReactiveFormsModule
    ],
    templateUrl: './register.component.html',
    styleUrl: './register.component.scss'
})
export class RegisterComponent
{
    registerForm: FormGroup

    constructor()
    {
        this.registerForm = new FormGroup({
            username: new FormControl(""),
            email: new FormControl(""),
            password: new FormControl(""),
            confirmPassword: new FormControl("")
        });
    }

    handleSubmit(){

    }
}
