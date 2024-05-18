import { HttpInterceptorFn } from '@angular/common/http';
import {TokenService} from "../token/token.service";
import {inject} from "@angular/core";

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService: TokenService = inject(TokenService);
  const authToken = tokenService.token as string;

  if(authToken){
    const authReq = req.clone({
      headers: req.headers.set('Authorization',`Bearer ${authToken}`)
    });
    return next(authReq);
  }
  return next(req);
};
