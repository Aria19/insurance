import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/users';
  
  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('No token found, authentication required!');
      throw new Error('Authentication required'); 
    }
    return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  }

  getUserById(id: number): Observable<any> { 
    try {
      const headers = this.getAuthHeaders(); 
      return this.http.get<any>(`${this.baseUrl}/${id}`, { headers })
        .pipe(
          catchError(error => {
            console.error('Error fetching user:', error);
            return throwError(() => new Error('Failed to fetch user data'));
          })
        );
    } catch (error) {
      return throwError(() => new Error('Authentication error: No token found'));
    }
  }
}
