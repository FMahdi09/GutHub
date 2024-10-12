import {HttpInterceptorFn} from '@angular/common/http';

const BASE_API_URL = "http://localhost:8080";

export const baseUrlInterceptor: HttpInterceptorFn = (req, next) =>
{
    const newReq = req.clone({url: `${BASE_API_URL}/${req.url}`});
    return next(newReq);
};
