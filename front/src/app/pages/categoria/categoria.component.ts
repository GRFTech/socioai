// categoria.component.ts
import { Component, OnInit } from '@angular/core';
import { CategoriaService, Categoria } from '../../services/categoria.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.scss']
})
export class CategoriaComponent implements OnInit {
  categorias: Categoria[] = [];
  form: FormGroup;

  constructor(private categoriaService: CategoriaService, private toast: ToastrService) {
    this.form = new FormGroup({
      nome: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
    this.loadCategorias();
  }

  loadCategorias() {
    this.categoriaService.getAll().subscribe(data => this.categorias = data);
  }

  createCategoria() {
    if (this.form.invalid) return;
    this.categoriaService.create(this.form.value).subscribe({
      next: (c) => {
        this.toast.success('Categoria criada com sucesso!');
        this.categorias.push(c);
        this.form.reset();
      },
      error: () => this.toast.error('Erro ao criar categoria!')
    });
  }

  updateCategoria(c: Categoria) {
    const nome = prompt('Editar nome da categoria:', c.nome);
    if (nome) {
      this.categoriaService.update(c.id!, { nome }).subscribe({
        next: (updated) => {
          this.toast.success('Categoria atualizada!');
          c.nome = updated.nome;
        },
        error: () => this.toast.error('Erro ao atualizar categoria!')
      });
    }
  }

  deleteCategoria(c: Categoria) {
    if (confirm(`Deseja realmente deletar "${c.nome}"?`)) {
      this.categoriaService.delete(c.id!).subscribe({
        next: () => {
          this.toast.success('Categoria deletada!');
          this.categorias = this.categorias.filter(cat => cat.id !== c.id);
        },
        error: () => this.toast.error('Erro ao deletar categoria!')
      });
    }
  }
}