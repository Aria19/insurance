import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/User';
import { UserService } from 'src/app/services/apiServices/userService/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User = {
    id: 0,
    username: '',
    email: '',
    role: '',
    image: '',
    password: ''
  };

  userId!: number;

  toastMessage = '';
  toastType = 'success';

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    const storedId = localStorage.getItem('userId');
    if (storedId) {
      this.userId = +storedId;
      console.log(this.userId);
      this.getUserById(this.userId);
    } else {
      console.log(this.userId);
      console.error("User not logged in.");
    }
  }

  getUserById(userId: number): void {
    this.userService.getUserById(userId).subscribe({
      next: (data) => {
        this.user = data;
      }
    })
  }

  onImageSelected(event: any, userId: number): void {
    const file: File = event.target.files[0];
    if (file) {
      this.userService.updateUserImage(userId, file).subscribe({
        next: () => {
          this.getUserById(userId);
          this.showToast('Image modifié avec succès.', 'success');
        },
        error: () => this.showToast("Erreur lors de la modification d'image.", 'danger')
      });
    }
  }

  showToast(message: string, type: 'success' | 'danger') {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => this.toastMessage = '', 5000);
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

  refresh(): void {
    window.location.reload();
  }
}