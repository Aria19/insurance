import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/authService/auth.service';

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css']
})
export class DashboardLayoutComponent implements OnInit {
  
  constructor(private authService: AuthService) {}
  
  name: string | null = '';
  role: string | null = '';

  ngOnInit(): void {
    this.name = localStorage.getItem('username');
    this.role = localStorage.getItem('role');
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }

  isAgent(): boolean {
    return this.role === 'AGENT';
  }

  onLogout() {
    this.authService.logout(); // Call logout method from AuthService
  }
}