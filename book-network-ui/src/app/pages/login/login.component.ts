import {Component, OnInit} from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../services/token/token.service";
import {KeycloakService} from "../../services/keycloak/keycloak.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit{

  constructor(
    private keycloakService: KeycloakService,
    ) {
  }

  //authRequest : AuthenticationRequest = {email: "", password : ""};
  //errorMessages: string[] = [];

  /*login() {
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
  }*/

  async ngOnInit() {
    await this.keycloakService.init();
    await this.keycloakService.login();
  }

  /*register(){
    this.router.navigate(["register"])
  }*/
}
