import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from "@angular/core";
import {KeycloakService} from "../keycloak/keycloak.service";

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService: KeycloakService = inject(KeycloakService);
  const authToken = keycloakService.keycloak?.token as  string;

  if(authToken){
    const authReq = req.clone({
      headers: req.headers.set('Authorization',`Bearer ${authToken}`)
    });
    return next(authReq);
  }
  return next(req);
};
