import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, EMPTY } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ContractService {
  private baseUrl = 'http://localhost:8080/productions';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders | null {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('No token found, authentication required!');
      return null;
    }
    return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  }

  getContracts(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    if (!headers) return EMPTY; // Return an empty observable if no token

    return this.http.get<any[]>(`${this.baseUrl}/view`, { headers }).pipe(
      catchError(err => {
        console.error('Error fetching contracts:', err);
        return EMPTY;
      })
    );
  }
  searchContracts(
    keyword?: string,
    risque?: string,
    codeRisque?: number,
    dateEffet?: number
  ): Observable<any[]> {
    const headers = this.getAuthHeaders();
    if (!headers) return EMPTY;
  
    let params = new HttpParams();
    if (keyword) params = params.set('keyword', keyword);
    if (risque) params = params.set('risque', risque);
    if (codeRisque !== null && codeRisque !== undefined)
      params = params.set('codeRisque', codeRisque.toString());
    if (dateEffet !== null && dateEffet !== undefined)
      params = params.set('dateEffet', dateEffet.toString());
  
    return this.http.get<any[]>(`${this.baseUrl}/search`, { headers, params });
  }
  
  exportContracts(keyword?: string, risk?: string, code?: number, dateEffet?: number): Observable<Blob> {
    const headers = this.getAuthHeaders();
    if (!headers) return EMPTY;

    let params = new HttpParams();
    if (keyword) params = params.set('keyword', keyword);
    if (risk) params = params.set('risk', risk);
    if (code) params = params.set('code', code.toString());
    if (dateEffet) params = params.set('dateEffet', dateEffet.toString());
  
    return this.http.get(`${this.baseUrl}/export`, { 
      params, 
      responseType: 'blob',
      headers: headers
    });
  }

  deleteContract(id: number): Observable<void> {
    const headers = this.getAuthHeaders();
    if (!headers) return EMPTY; 

    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`, { headers });
  }

  updateContract(contractId: number, contractData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    if (!headers) return EMPTY; 
    
    return this.http.put(`${this.baseUrl}/update/${contractId}`, contractData, { headers });
  }
  
  
}