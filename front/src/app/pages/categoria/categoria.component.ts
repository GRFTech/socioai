import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CategoriaService, CategoriaDTO, CategoriaResponse } from '../../services/categoria.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.scss']
})
export class CategoriaComponent implements OnInit {

  categorias: CategoriaResponse[] = [];
  novaCategoria = { nome: '' };     // <-- REMOVIDO username
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

  criar() {
    const username = this.authService.getUsername();
    if (!username) {
      console.error("Usuário não autenticado!");
      return;
    }

    const dto: CategoriaDTO = {
      nome: this.novaCategoria.nome,
      username: username            // <-- AGORA CORRETO
    };

    this.categoriaService.create(dto).subscribe({
      next: () => {
        this.novaCategoria.nome = ""; // limpa só nome
        this.load();
      }
    });
  }

  editar(c: CategoriaResponse) {
    this.editando = { ...c };
  }

  salvarEdicao() {
    if (!this.editando) return;

    const username = this.authService.getUsername()!;
    
    const dto: CategoriaDTO = {
      nome: this.editando.nome,
      username: username            // <-- USAR JWT
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
