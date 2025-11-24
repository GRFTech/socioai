import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SignUpComponent } from './pages/signup/signup.component';
import { HomeComponent } from './pages/home/home.component';
import { UserComponent } from './pages/user/user.component';
import { AuthGuardService } from './services/auth-guard.service';
import { CategoriaComponent } from './pages/categoria/categoria.component';
import { MetaComponent } from './pages/meta/meta.component'; // <-- ADICIONADO
import { LancamentoComponent } from './pages/lancamento/lancamento.component';
import { RelatorioComponent } from './pages/relatorio/relatorio.component';

export const routes: Routes = [
    { path: "", component: LoginComponent },
    { path: "signup", component: SignUpComponent },

    {
      path: "home",
      component: HomeComponent,
      canActivate: [AuthGuardService]
    },

    {
      path: "user",
      component: UserComponent,
      canActivate: [AuthGuardService]
    },

    {
      path: "categoria",
      component: CategoriaComponent,
      canActivate: [AuthGuardService]
    },

    {
      path: "metas",
      component: MetaComponent,
      canActivate: [AuthGuardService]
    },
    {
  path: "lancamentos",
  component: LancamentoComponent,
  canActivate: [AuthGuardService]
},
  {
    path: "user",
    component: UserComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: "relatorio",
    component: RelatorioComponent,
    canActivate: [AuthGuardService]
  }

];
