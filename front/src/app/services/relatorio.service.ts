import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TotalCategoriaResponse {
  categoria: string;
  valor: number;
}

@Injectable({
  providedIn: 'root'
})

export class RelatorioService {

  private apiUrl = "http://localhost:8080/api/categorias";

  constructor(private http: HttpClient) {}

  /**
   * Busca a lista de totais por categoria para um usuário específico.
   * Mapeia para GET /api/categorias/u/{username}/total
   */
  getTotaisPorUsuario(username: string): Observable<TotalCategoriaResponse[]> {
    return this.http.get<TotalCategoriaResponse[]>(`${this.apiUrl}/u/${username}/total`);
  }
}
