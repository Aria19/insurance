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

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    console.log('Login function triggered');

    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        console.log('Login successful:', response);

        // Save token and role in localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role);

        // Navigate all users to the same dashboard
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        alert('Login failed! Please check your credentials.');
      }
    });
  }
}