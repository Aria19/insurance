import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getUserById(id: number): Observable<any> {

    return this.http.get<any>(`${environment.apiUrl}/users/${id}`).pipe(
      catchError(error => {
        console.error('Error fetching user:', error);
        return throwError(() => new Error('Failed to fetch user data'));
      })
    );
  }

  getAllUsers(): Observable<any[]> {

    return this.http.get<any>(`${environment.apiUrl}/users`).pipe(
      catchError(error => {
        console.error('Error fetching user:', error);
        return throwError(() => new Error('Failed to fetch user data'));
      })
    );
  }

  addUser(userData: any): Observable<any> {

    return this.http.post<any>(`${environment.apiUrl}/users`, userData, {
      responseType: 'text' as 'json'
    });
  }

  updateUser(userId: number, userData: any): Observable<any> {

    return this.http.put(`${environment.apiUrl}/users/${userId}`, userData, {
      responseType: 'text' as 'json'
    });
  }

  updateUserPassword(userId: number, newPassword: string): Observable<string> {
    
    return this.http.put<string>(`${environment.apiUrl}/users/${userId}/password`, { password: newPassword }, {
      responseType: 'text' as 'json'
    });
  }

  updateUserImage(userId: number, image: File) {

    const formData = new FormData();
    formData.append('image', image);

    return this.http.put(`${environment.apiUrl}/users/${userId}/image`, formData, {
      responseType: 'text' as 'json'
    });
  }

  deleteUser(userId: number): Observable<void> {

    return this.http.delete<void>(`${environment.apiUrl}/users/${userId}`);
  }
}