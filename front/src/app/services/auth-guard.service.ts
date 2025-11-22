import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {

    if (!this.authService.isAuthenticated()) {
      this.authService.logout();
      this.router.navigate(['/login']); // ajuste conforme sua rota de login
      return false;
    }

    return true;
  }
}
