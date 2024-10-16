import {Injectable} from '@angular/core';
import {User} from "../models/user.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {RegisterDto} from "../../../../shared/Dtos/register.dto";

@Injectable({
    providedIn: 'root'
})
export class UserService
{
    private static readonly USER_API_PATH = "api/v1/users";

    private readonly http: HttpClient;

    constructor(http: HttpClient)
    {
        this.http = http;
    }

    getUsers(): Observable<Array<User>>
    {
        return this.http.get<Array<User>>(UserService.USER_API_PATH);
    }

    createUser(toCreate: RegisterDto): Observable<User>
    {
        return this.http.post<User>(UserService.USER_API_PATH, toCreate);
    }
}
