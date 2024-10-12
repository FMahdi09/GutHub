import {HttpInterceptorFn} from '@angular/common/http';
import {environment} from "../../../environments/environment";

export const baseUrlInterceptor: HttpInterceptorFn = (req, next) =>
{
    const newReq = req.clone({url: `${environment.base_api_url}/${req.url}`});
    return next(newReq);
};
