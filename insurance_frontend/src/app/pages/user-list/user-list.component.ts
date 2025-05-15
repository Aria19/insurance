import { Component, OnInit, ViewChild } from '@angular/core';
import { User } from 'src/app/models/User';
import { UserService } from 'src/app/services/apiServices/userService/user.service';
import { ModalComponent } from 'src/app/shared/modal/modal.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  errorMessage: string = '';
  userId!: number;
  @ViewChild(ModalComponent) deleteModal!: ModalComponent;
  private userIdToDelete: number | null = null;
  user: User | null = null;

  toastMessage = '';
  toastType = 'success';

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => this.users = data,
      error: (err) => this.errorMessage = err.message || 'Error fetching users'
    });
  }

  showAddForm = false;
  newUser = {
    username: '',
    email: '',
    password: '',
    role: ''
  };

  toggleAddForm() {
    this.showAddForm = !this.showAddForm;
  }

  submitAddUser() {
    this.userService.addUser(this.newUser).subscribe({
      next: () => {
        this.loadUsers();
        this.newUser = { username: '', email: '', password: '', role: '' };
        this.showAddForm = false;
        this.showToast('Utilisateur ajouté avec succès!', 'success');
      },
      error: (err) => {
        const msg = err.status === 409 ? '⚠️ Cet e-mail est déjà utilisé.' : 'Erreur: ' + (err.error?.message || "Quelque chose n'a pas fonctionné");
        this.showToast(msg, 'danger');
      }
    });
  }

  updateUserFields(user: User): void {
    this.userService.updateUser(user.id, user).subscribe({
      next: () => {
        this.showToast('Utilisateur modifié avec succès.', 'success');
      },
      error: (err) => {
        this.showToast("Erreur lors de la modification.", 'danger');
      }
    });
  }

  onImageSelected(event: any, userId: number): void {
    const file: File = event.target.files[0];
    if (file) {
      this.userService.updateUserImage(userId, file).subscribe({
        next: () => {
          this.loadUsers();
          this.showToast('Image modifié avec succès.', 'success');
        },
        error: () => this.showToast("Erreur lors de la modification d'image.", 'danger')
      });
    }
  }

  openDeleteModal(userId: number): void {
    this.userIdToDelete = userId;
    this.deleteModal.open();
  }

  deleteUser(): void {
    if (this.userIdToDelete !== null) {
      this.userService.deleteUser(this.userIdToDelete).subscribe({
        next: () => {
          this.showToast('Utilisateur supprimé avec succès.', 'success');
          this.loadUsers();
        },
        error: () => this.showToast("Erreur lors de la suppression de l'utilisateur.", 'danger')
      });
    }
  }

  showToast(message: string, type: 'success' | 'danger') {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => this.toastMessage = '', 5000);
  }

  reloadUsers() {
    this.loadUsers();
  }

  showPassword = false;

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  selectedUserId: number | null = null;
  newPassword: string = '';

  openPasswordModal(userId: number) {
    this.selectedUserId = userId;
    this.newPassword = '';
  }

  closeModal() {
    this.selectedUserId = null;
  }

  submitPasswordChange() {
    this.userService.updateUserPassword(this.selectedUserId!, this.newPassword).subscribe({
      next: () => {
        this.showToast("Mot de passe mis à jour avec succès.", "success");
        this.closeModal();
      },
      error: () => {
        this.showToast("Erreur lors de la mise à jour du mot de passe.", "danger");
      }
    });
  }

}