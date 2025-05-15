import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, EMPTY } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CreateProductionDTO } from 'src/app/models/create-production.dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContractService {

  constructor(private http: HttpClient) {
  }

  getContracts(): Observable<any[]> {

    return this.http.get<any[]>(`${environment.apiUrl}/productions/view`).pipe(
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
  
    let params = new HttpParams();
    if (keyword) params = params.set('keyword', keyword);
    if (risque) params = params.set('risque', risque);
    if (codeRisque !== null && codeRisque !== undefined)
      params = params.set('codeRisque', codeRisque.toString());
    if (dateEffet !== null && dateEffet !== undefined)
      params = params.set('dateEffet', dateEffet.toString());
  
    return this.http.get<any[]>(`${environment.apiUrl}/productions/search`, { params });
  }
  
  exportContracts(keyword?: string, risk?: string, code?: number, dateEffet?: number): Observable<Blob> {

    let params = new HttpParams();
    if (keyword) params = params.set('keyword', keyword);
    if (risk) params = params.set('risk', risk);
    if (code) params = params.set('code', code.toString());
    if (dateEffet) params = params.set('dateEffet', dateEffet.toString());
  
    return this.http.get(`${environment.apiUrl}/productions/export`, { 
      params, 
      responseType: 'blob'
    });
  }

  deleteContract(contractId: number): Observable<void> {
  
    return this.http.delete<void>(`${environment.apiUrl}/productions/delete/${contractId}`);
  }

  updateContract(contractId: number, contractData: any): Observable<any> {
    
    return this.http.put(`${environment.apiUrl}/productions/update/${contractId}`, contractData);
  }
  
  getContractsByContactId(contactId: number): Observable<any[]> {
    
    return this.http.get<any[]>(`${environment.apiUrl}/productions/contact/${contactId}`).pipe(
      catchError(err => {
        console.error('Error fetching contracts by contact ID:', err);
        return EMPTY;
      })
    );
  }
  
  addContractToContact(idContact: number, contractDto: CreateProductionDTO): Observable<void> {

    return this.http.post<void>(`${environment.apiUrl}/productions/add/${idContact}`, contractDto);
  }
}