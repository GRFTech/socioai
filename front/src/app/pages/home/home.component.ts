import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  username = localStorage.getItem("username") || "Usuário";

  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem("auth-token");
    localStorage.removeItem("username");
    this.router.navigate([""]); // redireciona para login
  }
}
