import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css']
})
export class DashboardLayoutComponent implements OnInit {
  
  constructor(private authService: AuthService) {}
  
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

  onLogout() {
    this.authService.logout(); // Call logout method from AuthService
  }
}
