import {Component, inject, OnInit} from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";
import {TokenService} from "../../../../services/token/token.service";
import {KeycloakService} from "../../../../services/keycloak/keycloak.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {


  constructor(private keycloakService: KeycloakService) {
  }

  private tokenService = inject(TokenService);
  username = "";

  ngOnInit(): void {
      const linkColor = document.querySelectorAll(".nav-link");
      linkColor.forEach(link => {
        if(window.location.href.endsWith(link.getAttribute("href") || "")){
          link.classList.add("active");
        }
        link.addEventListener('click', () => {
          linkColor.forEach(l => l.classList.remove("active"));
          link.classList.add("active");
        })
      })
    this.getUsernameFromToken();
  }


  async logout(){
    await this.keycloakService.keycloak?.logout();
  }

  private getUsernameFromToken(){
    const jwtHelperService = new JwtHelperService();
    this.username =  jwtHelperService.decodeToken(this.tokenService.token)["sub"];
  }
}
