// users.component.ts
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
// Importamos o Service e as Interfaces/Tipagens no mesmo import
import { UserService, UserResponse, UserDTO } from '../../services/user.service';
// Importe o RoleService (ou similar) aqui
// import { RoleService, RoleResponse } from '../../services/role.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user.component.html', // Usaremos o HTML sugerido anteriormente
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  users: UserResponse[] = [];
  roles: any[] = []; // Array para Roles (Papéis)

  // Usuário em edição (com campos de resposta e DTO)
  editando: (UserResponse & Partial<UserDTO>) | null = null;

  constructor(
    private userService: UserService,
    // private roleService: RoleService
  ) {}

  ngOnInit() {
    this.loadUsers();
    // this.loadRoles(); // Para popular o dropdown de edição
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe({
      next: data => this.users = data,
      error: err => console.error('Erro ao carregar usuários:', err)
    });
  }

  getRoleName(roleId: number): string {
    // Lógica para obter o nome do Papel a partir do ID
    // const role = this.roles.find(r => r.id === roleId);
    // return role ? role.name : 'N/A';
    return `Role ID: ${roleId}`; // Placeholder
  }

  editar(user: UserResponse) {
    // Inicia a edição, adicionando o campo 'password' vazio para a UI
    this.editando = { ...user, password: '' };
  }

  salvarEdicao() {
    if (!this.editando) return;

    const { username, password, roleId } = this.editando;

    const dto: Partial<UserDTO> = {
      username: username,
      password: password,
      roleId: roleId,
    };



    this.userService.updateUser(username, dto).subscribe({
      next: () => {
        this.editando = null;
        this.loadUsers();
      },
      error: err => console.error('Erro ao salvar edição do usuário:', err)
    });
  }

  cancelarEdicao() {
    this.editando = null;
  }

  deletar(id: string) {
    if (!confirm("Deseja realmente excluir este usuário?")) return;
    this.userService.deleteUser(id).subscribe({
      next: () => this.loadUsers(),
      error: err => console.error('Erro ao deletar usuário:', err)
    });
  }
}
