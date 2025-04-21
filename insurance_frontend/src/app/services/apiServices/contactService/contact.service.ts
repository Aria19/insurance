import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BaseService } from '../../baseService/base.service';
import { catchError, EMPTY, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContactService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getContacts():Observable<any[]>{
    const headers = this.getAuthHeaders();

    return this.http.get<any[]>(`${this.baseApiUrl}/contacts/view`, {headers}).pipe(
      catchError(err => {
        console.error("Error fetching contacts", err);
        return EMPTY;
      }
      )
    )
  }

  createContact(contactData: any): Observable<any> {
    const headers = this.getAuthHeaders();

    return this.http.post(`${this.baseApiUrl}/contacts/add`, contactData, {headers});
  }

  updateContact(contactId: number, contactData: any): Observable<any> {
    const headers = this.getAuthHeaders();

    return this.http.put(`${this.baseApiUrl}/contacts/update/${contactId}`, contactData, {
      headers,
      responseType: 'text' as 'json' 
    });
  }

  deleteContact(contactId: number): Observable<void> {
    const headers = this.getAuthHeaders();

    return this.http.delete<void>(`${this.baseApiUrl}/contacts/delete/${contactId}`, { headers });
  }

  deleteMultipleContacts(ids: number[]): Observable<string> {
    const headers = this.getAuthHeaders();
    return this.http.post<string>(`${this.baseApiUrl}/contacts/delete-multiple`, ids, {
      headers,
      responseType: 'text' as 'json' 
    });
  }

}
