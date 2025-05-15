import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { catchError, EMPTY, Observable } from 'rxjs';
import { Risque } from 'src/app/models/Risque';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RisqueService {
  constructor(private http: HttpClient) {
  }

  getAllRisques(): Observable<any[]> {

    return this.http.get<any[]>(`${environment.apiUrl}/risques/view`).pipe(
      catchError(err => {
        console.error("Error fetching risques", err);
        return EMPTY;
      })      
    )
  }

  

  deleteRisque(risqueId: number): Observable<void> {

    return this.http.delete<void>(`${environment.apiUrl}/risques/${risqueId}`);
  }
}
