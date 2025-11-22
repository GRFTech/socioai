import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CategoriaDTO {
  nome: string;
  username: string;
}

export interface CategoriaResponse {
  id: number;
  nome: string;
  tipo: string;
  user: string;  // <-- adicionado
}

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  private apiUrl = "http://localhost:8080/api/categorias";

  constructor(private http: HttpClient) {}

  getAll(): Observable<CategoriaResponse[]> {
    return this.http.get<CategoriaResponse[]>(this.apiUrl);
  }

  getByUsername(username: string): Observable<CategoriaResponse[]> {
    return this.http.get<CategoriaResponse[]>(`${this.apiUrl}/u/${username}`);
  }

  getById(id: number): Observable<CategoriaResponse> {
    return this.http.get<CategoriaResponse>(`${this.apiUrl}/${id}`);
  }

  create(dto: CategoriaDTO): Observable<CategoriaResponse> {
    return this.http.post<CategoriaResponse>(this.apiUrl, dto);
  }

  update(id: number, dto: CategoriaDTO): Observable<CategoriaResponse> {
    return this.http.put<CategoriaResponse>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  deleteAll(ids: number[]): Observable<void> {
    return this.http.request<void>('delete', `${this.apiUrl}/batch`, {
      body: ids
    });
  }
}
