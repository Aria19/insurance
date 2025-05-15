import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, EMPTY, Observable } from 'rxjs';
import { CreateContactDTO } from 'src/app/models/create-contact.dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  constructor(private http: HttpClient) {
  }

  getContacts(): Observable<any[]> {

    return this.http.get<any[]>(`${environment.apiUrl}/contacts/view`).pipe(
      catchError(err => {
        console.error("Error fetching contacts", err);
        return EMPTY;
      }
      )
    )
  }

  createContact(contactData: any): Observable<any> {

    return this.http.post(`${environment.apiUrl}/contacts/add`, contactData);
  }

  updateContact(contactId: number, contactData: any): Observable<any> {

    return this.http.put(`${environment.apiUrl}/contacts/update/${contactId}`, contactData, {
      responseType: 'text' as 'json'
    });
  }

  deleteContact(contactId: number): Observable<void> {

    return this.http.delete<void>(`${environment.apiUrl}/contacts/delete/${contactId}`);
  }

  deleteMultipleContacts(ids: number[]): Observable<string> {

    return this.http.post<string>(`${environment.apiUrl}/contacts/delete-multiple`, ids, {
      responseType: 'text' as 'json'
    });
  }

  importExcel(file: File): Observable<string> {

    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${environment.apiUrl}/import/excel`, formData, {
      responseType: 'text'
    });
  }

  searchContacts(
    assure?: string,
    msh?: string,
    societe?: string,
    codeRisque?: number,
    numeroContrat?: string
  ): Observable<CreateContactDTO[]> {

    let params = new HttpParams();

    if (assure) params = params.set('assure', assure);
    if (msh) params = params.set('msh', msh);
    if (societe) params = params.set('societe', societe);
    if (codeRisque !== undefined) params = params.set('codeRisque', codeRisque.toString());
    if (numeroContrat) params = params.set('numeroContrat', numeroContrat);

    return this.http.get<CreateContactDTO[]>(`${environment.apiUrl}/contacts/search`, { params });
  }

  exportContacts(
    assure: string,
    msh: string,
    societe: string,
    codeRisque: number | null,
    numeroContrat: string
  ): Observable<Blob> {

    let params = new HttpParams()
      .set('assure', assure)
      .set('msh', msh)
      .set('societe', societe)
      .set('numeroContrat', numeroContrat);

    if (codeRisque !== null) {
      params = params.set('codeRisque', codeRisque.toString());
    }

    return this.http.get(`${environment.apiUrl}/contacts/export`, {
      params: params,
      responseType: 'blob'
    });
  }

}
