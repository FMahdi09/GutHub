import {Component} from '@angular/core';
import {UserService} from "./services/user.service";
import {User} from "./models/user.model";
import {Observable} from "rxjs";
import {AsyncPipe} from "@angular/common";
import {ListItemComponent} from "../../../shared/components/list-item/list-item.component";

@Component({
    selector: 'app-user-list',
    standalone: true,
    imports: [
        AsyncPipe,
        ListItemComponent
    ],
    templateUrl: './user-list.component.html',
    styleUrl: './user-list.component.scss'
})
export class UserListComponent
{
    private readonly userService: UserService;

    users$!: Observable<Array<User>>;

    constructor(userService: UserService)
    {
        this.userService = userService;
    }

    ngOnInit(): void
    {
        this.users$ = this.userService.getUsers();
    }
}
