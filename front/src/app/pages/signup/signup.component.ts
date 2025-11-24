import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DefaultLoginLayoutComponent } from '../../components/default-login-layout/default-login-layout.component';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PrimaryInputComponent } from '../../components/primary-input/primary-input.component';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { ToastrService } from 'ngx-toastr';

interface SignupForm {
  email: FormControl,
  password: FormControl,
  passwordConfirm: FormControl
}

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    DefaultLoginLayoutComponent,
    ReactiveFormsModule,
    PrimaryInputComponent
  ],
  providers: [
    LoginService
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignUpComponent {
  signupForm!: FormGroup<SignupForm>;

  constructor(
    private router: Router,
    private loginService: LoginService,
    private toastService: ToastrService
  ){
    this.signupForm = new FormGroup({
  email: new FormControl(''),
  password: new FormControl('', [Validators.required, Validators.minLength(6)]),
  passwordConfirm: new FormControl('', [Validators.required, Validators.minLength(6)]),
})
  }

  submit() {
    const email = this.signupForm.value.email!;
    const password = this.signupForm.value.password!;
    const passwordConfirm = this.signupForm.value.passwordConfirm!;


    if (!email || email.trim() === '') {
      this.toastService.error("O campo de email não pode estar vazio.");
      return;
    }

    if (!email.includes('@')) {
      this.toastService.error("O email precisa conter o caractere '@'!");
      return;
    }


    if (password !== passwordConfirm) {
      this.toastService.error("As senhas não coincidem!");
      return;
    }

    this.loginService.signup(email, password).subscribe({
      next: () => this.toastService.success("Cadastro feito com sucesso!"),
      error: () => this.toastService.error("Erro inesperado! Tente novamente mais tarde")
    });
  }

  navigate(){
    this.router.navigate(["login"])
  }
}
