import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private router: Router) { }

  canActivate(): boolean {
    const token = localStorage.getItem("auth-token"); // verifica se existe token
    if (token) return true; // permite acesso à rota
    this.router.navigate([""]); // redireciona para login se não tiver token
    return false;
  }
}