import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { AuthenticationService } from "../../services/services/authentication.service"

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss']
})
export class ActivateAccountComponent {
  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  private confirmAccount(token: string) {
    this.authService.confirm({ token }).subscribe({
      next: () => {
        this.message = "Your account has been successfully activated.\nNow you can proceed to login.";
        this.submitted = true;
        this.isOkay = true;
      },
      error: (err) => {
        if (err.error) {
          this.message = JSON.parse(err.error)["businessExceptionDescription"];
          this.submitted = true;
          this.isOkay = false;
        }
      }
    });
  }
}
