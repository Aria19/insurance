import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export abstract class BaseService {
  protected baseApiUrl = 'http://localhost:8080';

  protected getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('No token found, authentication required!');
      throw new Error('Authentication required');
    }
    return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  }
}
