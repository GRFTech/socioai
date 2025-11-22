import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MetaDTO {
  descricao: string;
  valorAtual?: number;
  dataInicio?: string;
  dataFim?: string;
  categoria: number;
}

export interface MetaResponse {
  id: number;
  descricao: string;
  valorAtual?: number;
  dataInicio?: string;
  dataFim?: string;
  categoria: number;
  lancamentos: number[];
}

@Injectable({
  providedIn: 'root'
})
export class MetaService {
  private apiUrl = 'http://localhost:8080/api/metas';

  constructor(private http: HttpClient) {}

  getAll(): Observable<MetaResponse[]> {
    return this.http.get<MetaResponse[]>(this.apiUrl);
  }

  getByUsername(username: string): Observable<MetaResponse[]> {
    return this.http.get<MetaResponse[]>(`${this.apiUrl}/u/${username}`);
  }

  getById(id: number): Observable<MetaResponse> {
    return this.http.get<MetaResponse>(`${this.apiUrl}/${id}`);
  }

  create(dto: MetaDTO): Observable<MetaResponse> {
    return this.http.post<MetaResponse>(this.apiUrl, dto);
  }

  update(id: number, dto: MetaDTO): Observable<MetaResponse> {
    return this.http.put<MetaResponse>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  deleteAll(ids: number[]): Observable<void> {
    return this.http.request<void>('delete', `${this.apiUrl}/batch`, { body: ids });
  }
}
