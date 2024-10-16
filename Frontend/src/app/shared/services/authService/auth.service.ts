import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {LoginDto} from "../../Dtos/login.dto";
import {TokenDto} from "../../Dtos/token.dto";
import {catchError, map, Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthService
{
    isLoggedIn: boolean = false;

    private static readonly AUTH_API_ENDPOINT = "api/v1/auth";

    private readonly http: HttpClient;

    constructor(http: HttpClient)
    {
        this.http = http;
    }

    login(loginDto: LoginDto)
    {
        return this.http.post<TokenDto>(`${AuthService.AUTH_API_ENDPOINT}/login`, loginDto)
            .pipe(
                map(response =>
                {
                    this.isLoggedIn = true;
                    localStorage.setItem("accessToken", response.accessToken);
                }),
                catchError(error =>
                {
                    this.isLoggedIn = false;
                    return error;
                })
            )
    }
}
