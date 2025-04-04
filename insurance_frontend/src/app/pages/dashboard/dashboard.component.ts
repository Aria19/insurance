import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/authService/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private authService: AuthService) {}

  onLogout() {
    this.authService.logout(); // Call logout method from AuthService
  }

  role: string | null = '';

  ngOnInit(): void {
    this.role = localStorage.getItem('role'); // Retrieve role from localStorage
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }

  isAgent(): boolean {
    return this.role === 'AGENT';
  }
  
}