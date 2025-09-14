import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

interface LoginResponse {
  token: string;
  role: string;
  username: string;
  id: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private http: HttpClient, private router: Router) {}

  /** LOGIN FUNCTION */
  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, credentials).pipe(
      tap((response: LoginResponse) => {
        console.log('Login Response:', response); // Debugging output

        // Store token & role in localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role);

        if (response.username) {  // Ensure name exists before storing
          localStorage.setItem('username', response.username);
        } else {
          console.warn('No name found in login response');
        }

        localStorage.setItem('userId', response.id.toString());
      })
    );
  }


  /** CHECK IF USER IS LOGGED IN */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token'); // Returns true if token exists
  }

  /** GET USER ROLE */
  getRole(): string | null {
    return localStorage.getItem('role');
  }

  /** CHECK IF ADMIN */
  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  /** CHECK IF AGENT */
  isAgent(): boolean {
    return this.getRole() === 'AGENT';
  }

  logout() {
    localStorage.removeItem('token'); // Remove JWT
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }

  getToken() {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken(); // Check if token exists
  }
}