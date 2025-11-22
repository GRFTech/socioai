import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  getToken(): string | null {
    return localStorage.getItem("auth-token");
  }

  logout() {
    localStorage.removeItem("auth-token");
  }

  getPayload(): any | null {
    const token = this.getToken();
    if (!token) return null;

    const payload = token.split('.')[1];
    if (!payload) return null;

    try {
      return JSON.parse(atob(payload));
    } catch (e) {
      console.error("Erro ao decodificar JWT:", e);
      return null;
    }
  }

  isTokenExpired(): boolean {
    const payload = this.getPayload();
    if (!payload?.exp) return true;

    const expiry = payload.exp * 1000;  
    return Date.now() > expiry;
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;
    if (this.isTokenExpired()) return false;
    return true;
  }

  getUsername(): string | null {
    return this.getPayload()?.sub ?? null; // geralmente "sub" é o username/email
  }
}
