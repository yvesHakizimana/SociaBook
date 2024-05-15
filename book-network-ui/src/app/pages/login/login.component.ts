import { Component } from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../services/token/token.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService,
    ) {
  }

  authRequest : AuthenticationRequest = {email: "", password : ""};
  errorMessage: string[] = [];

  login() {
    this.errorMessage = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe(
      {
        next: (result) => {
          this.tokenService.token = result.token as string;
          this.router.navigate(['books']);
        },
        error: (err) => {
          if(err.error["validationErrors"]){
            this.errorMessage = err.error["validationErrors"];
          } else {
            this.errorMessage.push(err.error.error);
          }
        }
      }
    )
  }

  register(){
    this.router.navigate(["register"])
  }
}
