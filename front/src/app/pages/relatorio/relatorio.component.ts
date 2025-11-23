import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';

// Importações para o Gráfico
import { ChartData, ChartOptions } from 'chart.js';
import { ChartModule } from 'primeng/chart';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';

// Assumindo que você tem um arquivo de serviços para o relatório
import { RelatorioService, TotalCategoriaResponse } from '../../services/relatorio.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-relatorio',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    CardModule,
    ChartModule, // Deve ser importado para o p-chart funcionar
    CurrencyPipe
  ],
  templateUrl: './relatorio.component.html',
  styleUrl: './relatorio.component.scss'
})
export class RelatorioComponent implements OnInit {

  // Variáveis de estado para o gráfico - RENOMEADAS para o padrão 'basic'
  basicData: ChartData | undefined;
  basicOptions: ChartOptions | undefined;

  // Variáveis de estado para a tabela
  totaisPorCategoria: TotalCategoriaResponse[] = [];
  loading: boolean = false;

  constructor(
    private relatorioService: RelatorioService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.loadTotais();
    this.initChartOptions();
  }

  /**
   * Executa a chamada HTTP para obter o relatório de totais do usuário logado.
   */
  loadTotais() {
    this.loading = true;
    const username = this.authService.getUsername();

    if (!username) {
      console.error("Usuário não autenticado. Não foi possível carregar o relatório.");
      this.loading = false;
      return;
    }

    this.relatorioService.getTotaisPorUsuario(username).subscribe({
      next: (data: TotalCategoriaResponse[]) => {
        this.totaisPorCategoria = data;
        this.loading = false;
        this.updateChartData(data); // Chama a função para atualizar basicData
      },
      error: (err) => {
        console.error('Erro ao carregar o relatório de totais:', err);
        this.loading = false;
      }
    });
  }

  // Função para inicializar opções básicas do gráfico - ADAPTADA AO ESTILO DO PRIMENG DEMO
  initChartOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    // Usando variáveis CSS com o prefixo '--p-' conforme o demo
    const textColor = documentStyle.getPropertyValue('--p-text-color') || '#495057';
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color') || '#6c757d';
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color') || '#dee2e6';

    this.basicOptions = { // RENOMEADO
      plugins: {
        legend: {
          labels: {
            color: textColor,
          },
          display: false // Mantendo o display off para série única
        },
        title: {
          display: true,
          text: 'Distribuição de Valores por Categoria',
          font: {
            size: 16
          },
          color: textColor
        }
      },
      scales: {
        x: {
          ticks: {
            color: textColorSecondary,
            font: {
              weight: 'bold' as const
            }
          },
          grid: {
            color: surfaceBorder,
            drawOnChartArea: false
          },
        },
        y: {
          beginAtZero: true,
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
          },
        },
      },
    };
  }

  // Função para transformar os dados do relatório no formato do Chart.js
  updateChartData(data: TotalCategoriaResponse[]) {
    // Extrai os nomes das categorias para os rótulos do eixo X
    const labels = data.map(item => item.categoria);
    // Extrai os valores para a série de dados do gráfico
    const values = data.map(item => item.valor);

    const documentStyle = getComputedStyle(document.documentElement);
    // Cor primária do tema PrimeNG (usada para as barras)
    const primaryColor = documentStyle.getPropertyValue('--p-primary-color') || '#42A5F5';

    this.basicData = { // RENOMEADO
      labels: labels, // Nomes das categorias
      datasets: [
        {
          label: 'Valor Total',
          data: values, // Valores de cada barra
          backgroundColor: primaryColor + '90', // Cor com transparência
          borderColor: primaryColor,
          borderWidth: 1,
          borderRadius: 4
        }
      ]
    };
  }
}
