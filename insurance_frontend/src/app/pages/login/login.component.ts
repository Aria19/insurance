import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/authService/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginData = { email: '', password: '' };

  constructor(private authService: AuthService, private router: Router) { }

  loginErrorMessage: string = '';

  onLogin(): void {
    console.log('Login function triggered');

    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        console.log('Login successful:', response);

        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role);

        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Login failed:', err);

        this.loginErrorMessage = 'Adresse e-mail ou mot de passe incorrect';
        this.loginData.email = '';
        this.loginData.password = '';
      }
    });
  }

  showPassword = false;

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}