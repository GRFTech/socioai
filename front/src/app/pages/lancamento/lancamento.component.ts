import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Seus serviços
import { LancamentoService, LancamentoDTO, LancamentoResponse } from '../../services/lancamento.service';
import { MetaService, MetaResponse } from '../../services/meta.service';
import { AuthService } from '../../services/auth.service';

// PrimeNG Imports
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';

@Component({
  selector: 'app-lancamento',
  standalone: true,
  // Adicionei os módulos necessários para os filtros e ícones
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    TagModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    DropdownModule,
    MultiSelectModule
  ],
  templateUrl: './lancamento.component.html',
  styleUrls: ['./lancamento.component.scss']
})
export class LancamentoComponent implements OnInit {

  // Referência para a tabela para poder usar o clear()
  @ViewChild('dt1') dt1!: Table;

  lancamentos: LancamentoResponse[] = [];
  metas: MetaResponse[] = [];
  loading: boolean = true; // Controle de carregamento

  novaLancamento: Partial<LancamentoDTO> = {
    descricao: '',
    valor: undefined,
    meta: undefined
  };

  editando: LancamentoResponse | null = null;

  constructor(
    private lancamentoService: LancamentoService,
    private metaService: MetaService,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.loadMetas();
    this.loadLancamentos();
  }

  loadLancamentos() {
    const username = this.auth.getUsername();
    if (!username) return;

    this.loading = true; // Ativa loading
    this.lancamentoService.getByUsername(username).subscribe({
      next: data => {
        this.lancamentos = data;
        this.loading = false; // Desativa loading
      },
      error: err => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  loadMetas() {
    const username = this.auth.getUsername();
    if (!username) return;

    this.metaService.getByUsername(username).subscribe({
      next: data => this.metas = data,
      error: err => console.error(err)
    });
  }

  // --- Funções Auxiliares da Tabela PrimeNG ---

  clear(table: Table) {
    table.clear();
  }

  // Define a cor da Tag baseada se é Receita ou Despesa
  getSeverity(valor: number): "success" | "danger" | "info" {
    if (valor > 0) return 'success'; // Receita (Verde)
    if (valor < 0) return 'danger';  // Despesa (Vermelho)
    return 'info';
  }

  // Helper para pegar o nome da meta dentro do template se necessário
  getMetaNome(id: number | undefined) {
    return this.metas.find(m => m.id === id)?.descricao || "Sem meta";
  }

  // --- Funções CRUD (Mantidas iguais) ---

  criar() {
    if (!this.novaLancamento.descricao || !this.novaLancamento.valor || !this.novaLancamento.meta) {
      alert("Preencha todos os campos!");
      return;
    }
    // ... logica de criar mantida igual
    const dto: LancamentoDTO = {
      descricao: this.novaLancamento.descricao,
      valor: this.novaLancamento.valor,
      dataCriacao: new Date(),
      meta: this.novaLancamento.meta
    };

    this.lancamentoService.create(dto).subscribe({
      next: () => {
        this.resetForm();
        this.loadLancamentos();
      },
      error: err => console.error(err)
    });
  }

  // Métodos editar, salvarEdicao, cancelarEdicao, deletar, resetForm...
  // (Mantenha os que você já tinha no seu código original)
  editar(item: LancamentoResponse) { this.editando = { ...item }; }

  salvarEdicao() {
    if (!this.editando) return;
    // Lógica de update mantida...
    const dto: Partial<LancamentoDTO> = {
      descricao: this.editando.descricao,
      valor: this.editando.valor,
      dataCriacao: new Date(this.editando.dataCriacao),
      meta: this.editando.meta
    };
    this.lancamentoService.update(this.editando.id, dto).subscribe({
      next: () => { this.editando = null; this.loadLancamentos(); },
      error: err => console.error(err)
    });
  }

  cancelarEdicao() { this.editando = null; }

  deletar(id: number) {
    if (!confirm("Tem certeza?")) return;
    this.lancamentoService.delete(id).subscribe({
      next: () => this.loadLancamentos(),
      error: err => console.error(err)
    });
  }

  resetForm() {
    this.novaLancamento = { descricao: '', valor: undefined, meta: undefined };
  }
}
