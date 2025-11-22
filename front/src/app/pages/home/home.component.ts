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

  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem("auth-token");
    localStorage.removeItem("username");
    this.router.navigate([""]); // redireciona para login
  }

  data: any;

  options: any;

  platformId = inject(PLATFORM_ID);

  ngOnInit() {
    this.initChart();
  }

  initChart() {
    if (isPlatformBrowser(this.platformId)) {
      const documentStyle = getComputedStyle(document.documentElement);
      const textColor = documentStyle.getPropertyValue('--p-text-color');
      const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');

      this.data = {
        labels: ['MONSTRUOSAMENTE', 'PRA K7', 'EXCELENTEMENTE', 'CAVEIRA', 'TOP FUNCIONALIDADES', 'COMANDOOOOOS', 'CRISTIANO RONAAAALDO'],
        datasets: [
          {
            label: 'PRIME NG FUNCIONANDO',
            borderColor: documentStyle.getPropertyValue('--p-gray-400'),
            pointBackgroundColor: documentStyle.getPropertyValue('--p-gray-400'),
            pointBorderColor: documentStyle.getPropertyValue('--p-gray-400'),
            pointHoverBackgroundColor: textColor,
            pointHoverBorderColor: documentStyle.getPropertyValue('--p-gray-400'),
            data: [99, 99, 99, 99, 99, 99, 99]
          },
          {
            label: 'DÚVIDAS SENHORES',
            borderColor: documentStyle.getPropertyValue('--p-cyan-400'),
            pointBackgroundColor: documentStyle.getPropertyValue('--p-cyan-400'),
            pointBorderColor: documentStyle.getPropertyValue('--p-cyan-400'),
            pointHoverBackgroundColor: textColor,
            pointHoverBorderColor: documentStyle.getPropertyValue('--p-cyan-400'),
            data: [0, 0, 0, 0, 0, 0, 0]
          }
        ]
      };

      this.options = {
        plugins: {
          legend: {
            labels: {
              color: textColor
            }
          }
        },
        scales: {
          r: {
            grid: {
              color: textColorSecondary
            }
          }
        }
      };
    }
  }
}
