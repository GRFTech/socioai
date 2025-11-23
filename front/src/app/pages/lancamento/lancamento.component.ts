import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LancamentoService, LancamentoDTO, LancamentoResponse } from '../../services/lancamento.service';
import { MetaService, MetaResponse } from '../../services/meta.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-lancamento',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lancamento.component.html',
  styleUrls: ['./lancamento.component.scss']
})
export class LancamentoComponent implements OnInit {

  lancamentos: LancamentoResponse[] = [];
  metas: MetaResponse[] = [];

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
    this.loadLancamentos();
    this.loadMetas();
  }

  loadLancamentos() {
    const username = this.auth.getUsername();
    if (!username) return;

    this.lancamentoService.getByUsername(username).subscribe({
      next: data => this.lancamentos = data,
      error: err => console.error(err)
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

  criar() {
    if (!this.novaLancamento.descricao || !this.novaLancamento.valor || !this.novaLancamento.meta) {
      alert("Preencha todos os campos!");
      return;
    }

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

  editar(item: LancamentoResponse) {
    this.editando = { ...item };
  }

  salvarEdicao() {
    if (!this.editando) return;

    const dto: Partial<LancamentoDTO> = {
      descricao: this.editando.descricao,
      valor: this.editando.valor,
      dataCriacao: new Date(this.editando.dataCriacao),
      meta: this.editando.meta
    };

    this.lancamentoService.update(this.editando.id, dto).subscribe({
      next: () => {
        this.editando = null;
        this.loadLancamentos();
      },
      error: err => console.error(err)
    });
  }

  cancelarEdicao() {
    this.editando = null;
  }

  deletar(id: number) {
    if (!confirm("Tem certeza que deseja excluir?")) return;

    this.lancamentoService.delete(id).subscribe({
      next: () => this.loadLancamentos(),
      error: err => console.error(err)
    });
  }

  getMetaNome(id: number | undefined) {
    return this.metas.find(m => m.id === id)?.descricao || "Sem meta";
  }

  resetForm() {
    this.novaLancamento = {
      descricao: '',
      valor: undefined,
      meta: undefined
    };
  }
}
