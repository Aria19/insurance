import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/services/authService/auth.service';

declare const bootstrap: any;

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css']
})
export class DashboardLayoutComponent implements OnInit {
  
  @ViewChild('sidebarRef', { static: false }) sidebarRef!: ElementRef;
  
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

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const sidebar = this.sidebarRef?.nativeElement;
    const clickedInside = sidebar?.contains(event.target);
    const isSidebarVisible = sidebar?.classList.contains('show');

    if (!clickedInside && isSidebarVisible) {
      // Hide the sidebar
      const bsCollapse = bootstrap.Collapse.getInstance(sidebar);
      if (bsCollapse) {
        bsCollapse.hide();
      }
    }
  }
}