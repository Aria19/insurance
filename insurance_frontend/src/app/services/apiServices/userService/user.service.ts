import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BaseService } from '../../baseService/base.service';

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getUserById(id: number): Observable<any> {
    const headers = this.getAuthHeaders();
  
    return this.http.get<any>(`${this.baseApiUrl}/${id}`, { headers }).pipe(
      catchError(error => {
        console.error('Error fetching user:', error);
        return throwError(() => new Error('Failed to fetch user data'));
      })
    );
  }
  
}
