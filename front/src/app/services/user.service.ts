// user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// --- Interfaces (Tipagens) incluídas no Service ---

/**
 * Interface baseada no AuthenticatedUserEntityDTO do seu backend Java
 */
export interface UserDTO {
  username: string; // O email
  password?: string; // Opcional para edição, mas obrigatório para criação
  roleId: number; // Tipo Long no Java -> number no TS
}

/**
 * Interface baseada no AuthenticatedUserEntityResponse do seu backend Java
 */
export interface UserResponse {
  id: string; // UUID no Java -> string no TS
  username: string;
  roleId: number;
  // Adicione quaisquer outros campos que a resposta contenha
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // URL base para o endpoint de usuários
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  /**
   * GET /api/users
   */
  getAllUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.apiUrl);
  }

  /**
   * GET /api/users/{id}
   */
  getById(id: string): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * PUT /api/users/{username}
   */
  updateUser(username: string, dto: Partial<UserDTO>): Observable<UserResponse> {
    const { password, ...updateDTO } = dto;

    // Se a senha foi preenchida, envie ela. Caso contrário, envie apenas os outros campos.
    const body: Partial<UserDTO> = password ? { ...updateDTO, password } : updateDTO;

    return this.http.put<UserResponse>(`${this.apiUrl}/${username}`, body);
  }

  /**
   * DELETE /api/users/{id}
   */
  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * DELETE /api/users/batch
   */
  deleteAllUsers(ids: string[]): Observable<void> {
    return this.http.request<void>('delete', `${this.apiUrl}/batch`, { body: ids });
  }
}
