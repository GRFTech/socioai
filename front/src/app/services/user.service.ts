import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// --- Interfaces (Tipagens) incluídas no Service ---

export interface UserDTO {
  username?: string;
  password?: string;
  roleId: number; // Campo obrigatório para atualização de papel
}

export interface UserResponse {
  id: string;
  username: string;
  role: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.apiUrl);
  }

  getById(id: string): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * PUT /api/users/{username}
   * Garante que o body contenha roleId, username (para segurança do backend)
   * e o password (somente se não for vazio).
   */
  updateUser(username: string, dto: Partial<UserDTO>): Observable<UserResponse> {

    const body: Partial<UserDTO> = {};

    // 1. Envia roleId (Campo obrigatório para o update no backend)
    if (dto.roleId !== undefined && dto.roleId !== null) {
      body.roleId = dto.roleId;
    }

    // 2. Envia username (Recomendado, pois o DTO do backend pode exigir)
    // Usamos o username do DTO, se ele existir, ou do path se não for um campo de edição.
    body.username = dto.username || username;

    // 3. Inclui o password APENAS se a string não for vazia (usuário digitou)
    if (dto.password) {
      body.password = dto.password;
    }

    return this.http.put<UserResponse>(`${this.apiUrl}/${username}`, body);
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  deleteAllUsers(ids: string[]): Observable<void> {
    return this.http.request<void>('delete', `${this.apiUrl}/batch`, { body: ids });
  }
}
