import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MetaService, MetaDTO, MetaResponse } from '../../services/meta.service';
import { CategoriaService, CategoriaResponse } from '../../services/categoria.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-meta',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './meta.component.html',
  styleUrls: ['./meta.component.scss']
})
export class MetaComponent implements OnInit {

  metas: MetaResponse[] = [];
  categorias: CategoriaResponse[] = [];

  novaMeta: Partial<MetaDTO> = { descricao: '', valorAtual: 0 };
  editando: MetaResponse | null = null;

  constructor(
    private metaService: MetaService,
    private categoriaService: CategoriaService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.loadMetas();
    this.loadCategorias();
  }

  getCategoriaNome(categoriaId: number | undefined): string {
  if (!categoriaId) return '';
  const categoria = this.categorias.find(c => c.id === categoriaId);
  return categoria ? categoria.nome : '';
}

  // Carregar metas do usuário logado
  loadMetas() {
    const username = this.authService.getUsername();
    if (!username) return console.error("Usuário não autenticado!");
    this.metaService.getByUsername(username).subscribe({
      next: data => this.metas = data,
      error: err => console.error(err)
    });
  }

  // Carregar categorias do usuário logado
  loadCategorias() {
    const username = this.authService.getUsername();
    if (!username) return;
    this.categoriaService.getByUsername(username).subscribe({
      next: data => this.categorias = data,
      error: err => console.error(err)
    });
  }

  // Criar nova meta
  criar() {
    const username = this.authService.getUsername();
    if (!username) return console.error("Usuário não autenticado!");

    if (!this.novaMeta.descricao || !this.novaMeta.categoria) {
      console.error("Descrição e categoria são obrigatórias!");
      return;
    }

    const dto: MetaDTO = {
      descricao: this.novaMeta.descricao!,
      valorAtual: this.novaMeta.valorAtual,
      dataInicio: this.novaMeta.dataInicio,
      dataFim: this.novaMeta.dataFim,
      categoria: this.novaMeta.categoria!
    };

    this.metaService.create(dto).subscribe({
      next: () => {
        this.novaMeta = { descricao: '', valorAtual: 0 };
        this.loadMetas();
      },
      error: err => console.error(err)
    });
  }

  // Preparar meta para edição
  editar(meta: MetaResponse) {
    this.editando = { ...meta };
  }

  // Salvar edição
  salvarEdicao() {
    if (!this.editando) return;

    const dto: MetaDTO = {
      descricao: this.editando.descricao,
      valorAtual: this.editando.valorAtual,
      dataInicio: this.editando.dataInicio,
      dataFim: this.editando.dataFim,
      categoria: this.editando.categoria // categoria não pode ser alterada
    };

    this.metaService.update(this.editando.id, dto).subscribe({
      next: () => {
        this.editando = null;
        this.loadMetas();
      },
      error: err => console.error(err)
    });
  }

  cancelarEdicao() {
    this.editando = null;
  }

  deletar(id: number) {
    if (!confirm("Deseja realmente excluir esta meta?")) return;
    this.metaService.delete(id).subscribe({ next: () => this.loadMetas() });
  }
}
