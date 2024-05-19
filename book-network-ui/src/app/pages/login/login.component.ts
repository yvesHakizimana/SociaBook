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
  errorMessages: string[] = [];

  login() {
    this.errorMessages = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe(
      {
        next: (result) => {
          this.tokenService.token = result.token as string;
          this.router.navigate(['books']);
        },
        error: (err) => {

          console.log(err);
        }
      }
    )
  }

  register(){
    this.router.navigate(["register"])
  }
}
