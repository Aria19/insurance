import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, EMPTY } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BaseService } from '../../baseService/base.service';

@Injectable({
  providedIn: 'root'
})
export class ContractService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getContracts(): Observable<any[]> {
    const headers = this.getAuthHeaders();

    return this.http.get<any[]>(`${this.baseApiUrl}/productions/view`, { headers }).pipe(
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
  
    let params = new HttpParams();
    if (keyword) params = params.set('keyword', keyword);
    if (risque) params = params.set('risque', risque);
    if (codeRisque !== null && codeRisque !== undefined)
      params = params.set('codeRisque', codeRisque.toString());
    if (dateEffet !== null && dateEffet !== undefined)
      params = params.set('dateEffet', dateEffet.toString());
  
    return this.http.get<any[]>(`${this.baseApiUrl}/productions/search`, { headers, params });
  }
  
  exportContracts(keyword?: string, risk?: string, code?: number, dateEffet?: number): Observable<Blob> {
    const headers = this.getAuthHeaders();

    let params = new HttpParams();
    if (keyword) params = params.set('keyword', keyword);
    if (risk) params = params.set('risk', risk);
    if (code) params = params.set('code', code.toString());
    if (dateEffet) params = params.set('dateEffet', dateEffet.toString());
  
    return this.http.get(`${this.baseApiUrl}/productions/export`, { 
      params, 
      responseType: 'blob',
      headers: headers
    });
  }

  deleteContract(id: number): Observable<void> {
    const headers = this.getAuthHeaders();

    return this.http.delete<void>(`${this.baseApiUrl}/productions/delete/${id}`, { headers });
  }

  updateContract(contractId: number, contractData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    
    return this.http.put(`${this.baseApiUrl}/productions/update/${contractId}`, contractData, { headers });
  }
  
  getContractsByContactId(contactId: number): Observable<any[]> {
    const headers = this.getAuthHeaders();
    
    return this.http.get<any[]>(`${this.baseApiUrl}/productions/contact/${contactId}`, { headers }).pipe(
      catchError(err => {
        console.error('Error fetching contracts by contact ID:', err);
        return EMPTY;
      })
    );
  }
  
}