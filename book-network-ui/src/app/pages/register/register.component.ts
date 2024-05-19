import { Component } from '@angular/core';
import { RegistrationRequest } from "../../services/models/registration-request";
import { Router } from "@angular/router";
import { AuthenticationService } from "../../services/services/authentication.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'] // Fixed here
})
export class RegisterComponent {

  registerRequest: RegistrationRequest = { email: "", password: "", firstName: "", lastName: "" };
  errorMessages: string[] = [];

  constructor(
    private router: Router,
    private registerService: AuthenticationService
  ) { }

  login() {
    this.router.navigate(["login"]);
  }

  register() {
    this.errorMessages = [];
    this.registerService.register({
      body: this.registerRequest
    }).subscribe({
      next: () => {
        this.router.navigate(["activate-account"])
      },
      error: (err) => {
        if (err.error && err.error["validationErrors"]) {
          this.errorMessages = err.error["validationErrors"];
        } else if (err.error && err.error["businessExceptionDescription"]) {
          this.errorMessages.push(err.error["businessExceptionDescription"]);
        } else {
          this.errorMessages.push("An unexpected error occurred.");
          console.log(err);
        }
      }
    });
  }
}
