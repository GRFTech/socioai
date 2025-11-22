import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// FRONT envia Date (apenas ao criar ou editar)
export interface LancamentoDTO {
  descricao: string;
  valor: number;
  dataCriacao: Date;
  meta: number;
}

// BACK retorna data como STRING
export interface LancamentoResponse {
  id: number;
  descricao: string;
  valor: number;
  meta?: number;
  dataCriacao: string; // <-- IMPORTANTE: backend retorna string
}

// Tipo usado para enviar ao backend
export interface LancamentoDTOEnviado extends Omit<LancamentoDTO, 'dataCriacao'> {
  dataCriacao: string; // <-- enviado como string
}

@Injectable({
  providedIn: 'root'
})
export class LancamentoService {

  private baseUrl = 'http://localhost:8080/api/lancamentos';

  constructor(private http: HttpClient) {}

  private formatDate(date: Date | string): string {
    const d = typeof date === 'string' ? new Date(date) : date;
    return d.toISOString().slice(0, 19);
  }

  create(lancamento: LancamentoDTO): Observable<LancamentoResponse> {
    const payload: LancamentoDTOEnviado = {
      ...lancamento,
      dataCriacao: this.formatDate(lancamento.dataCriacao)
    };
    return this.http.post<LancamentoResponse>(this.baseUrl, payload);
  }

  update(id: number, lancamento: Partial<LancamentoDTO>): Observable<LancamentoResponse> {
    const payload: Partial<LancamentoDTOEnviado> = {};

    if (lancamento.descricao !== undefined) payload.descricao = lancamento.descricao;
    if (lancamento.valor !== undefined) payload.valor = lancamento.valor;
    if (lancamento.dataCriacao !== undefined) {
      payload.dataCriacao = this.formatDate(lancamento.dataCriacao);
    }
    if(lancamento.meta !== undefined) payload.meta = lancamento.meta;

    return this.http.put<LancamentoResponse>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getAll(): Observable<LancamentoResponse[]> {
    return this.http.get<LancamentoResponse[]>(this.baseUrl);
  }

  getById(id: number): Observable<LancamentoResponse> {
    return this.http.get<LancamentoResponse>(`${this.baseUrl}/${id}`);
  }

  getByUsername(username: string): Observable<LancamentoResponse[]> {
    return this.http.get<LancamentoResponse[]>(`${this.baseUrl}/u/${username}`);
  }
}
