import { Component } from '@angular/core';
import {RegistrationRequest} from "../../services/models/registration-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registerRequest : RegistrationRequest = {email: "", password: "", firstName: "", lastName: ""};
  errorMessages: string[] = [];

  constructor(
    private router: Router,
    private registerService: AuthenticationService
              ) {
  }
  login(){
    this.router.navigate(["login"])
  }

  register(){
    this.errorMessages = [];
    this.registerService.register({
      body:  this.registerRequest
    }).subscribe({
      next: (result) => {
        this.router.navigate(["activate-account"])
      },
      error: (err) => {
        if(err.error["validationErrors"]){
          this.errorMessages = err.error["validationErrors"];
        } else {
          this.errorMessages.push(err.error.error);
        }
      }

    })
  }
}
