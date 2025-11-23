import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CategoriaService, CategoriaDTO, CategoriaResponse } from '../../services/categoria.service';
import { AuthService } from '../../services/auth.service';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext'; // Para o input no modal

import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule,ButtonModule,DialogModule,InputTextModule],
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.scss']
})
export class CategoriaComponent implements OnInit {

  categorias: CategoriaResponse[] = [];

  // 1. Variável para controlar a visibilidade do Modal
  displayModal: boolean = false;

  novaCategoria = { nome: '' };     // <-- Dados do formulário dentro do modal
  editando: CategoriaResponse | null = null;

  constructor(
    private categoriaService: CategoriaService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    const username = this.authService.getUsername(); // pega o usuário logado
    if (!username) {
      console.error("Usuário não autenticado!");
      return;
    }

    this.categoriaService.getByUsername(username).subscribe({
      next: (data) => this.categorias = data,
      error: (err) => console.error(err)
    });
  }

  // 2. Função para abrir o Modal (chamada pelo botão 'Criar Nova Categoria')
  mostrarModal() {
    this.novaCategoria.nome = ''; // Limpa o nome ao abrir
    this.displayModal = true;
  }

  // 3. Função para fechar o Modal (chamada pelo botão 'Cancelar')
  fecharModal() {
    this.displayModal = false;
  }

  // 4. Função que executa a criação E fecha o Modal (chamada pelo botão 'Salvar' no modal)
  criarCategoriaEFechar() {
    const username = this.authService.getUsername();
    if (!username) {
      console.error("Usuário não autenticado!");
      return;
    }

    const dto: CategoriaDTO = {
      nome: this.novaCategoria.nome,
      username: username
    };

    this.categoriaService.create(dto).subscribe({
      next: () => {
        this.novaCategoria.nome = "";
        this.load();
        this.fecharModal(); // Fecha o modal após a criação bem-sucedida
      },
      error: (err) => {
        console.error("Erro ao criar categoria:", err);
        // Opcional: Manter o modal aberto para correção ou fechar
      }
    });
  }

  // 5. O método 'criar()' original foi removido/substituído pela lógica em 'criarCategoriaEFechar()'
  //    para garantir que a criação seja feita via modal.

  editar(c: CategoriaResponse) {
    this.editando = { ...c };
  }

  salvarEdicao() {
    if (!this.editando) return;

    const username = this.authService.getUsername()!;

    const dto: CategoriaDTO = {
      nome: this.editando.nome,
      username: username
    };

    this.categoriaService.update(this.editando.id, dto).subscribe({
      next: () => {
        this.editando = null;
        this.load();
      }
    });
  }

  cancelarEdicao() {
    this.editando = null;
  }

  deletar(id: number) {
    if (!confirm("Deseja realmente excluir?")) return;

    this.categoriaService.delete(id).subscribe({
      next: () => this.load()
    });
  }
}
