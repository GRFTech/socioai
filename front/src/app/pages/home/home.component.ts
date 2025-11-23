import {ChangeDetectorRef, Component, effect, inject, OnInit, PLATFORM_ID} from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import {ChartModule} from "primeng/chart";
import {isPlatformBrowser} from "@angular/common";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, ChartModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  username = localStorage.getItem("username") || "Usuário";

  // O menu começa FECHADO
  isMenuOpen: boolean = false;

  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem("auth-token");
    localStorage.removeItem("username");
    this.router.navigate([""]); // redireciona para login
  }

  // Função que inverte o estado (abre/fecha o menu)
  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  // Função para fechar o menu após a navegação
  closeMenu() {
    if (this.isMenuOpen) {
      this.isMenuOpen = false;
    }
  }

  data: any;

  options: any;

  platformId = inject(PLATFORM_ID);

  initChart() {
    if (isPlatformBrowser(this.platformId)) {
      this.data = {
        datasets: []
      };
      this.options = {};
    }
  }

  ngOnInit() {
    this.initChart();
  }

}
