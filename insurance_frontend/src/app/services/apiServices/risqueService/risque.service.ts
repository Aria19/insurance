import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { catchError, EMPTY, Observable } from 'rxjs';
import { BaseService } from '../../baseService/base.service';
import { Risque } from 'src/app/models/Risque';

@Injectable({
  providedIn: 'root'
})
export class RisqueService extends BaseService {
  constructor(private http: HttpClient) {
    super();
  }

  getAllRisques(): Observable<any[]> {
    const headers = this.getAuthHeaders();

    return this.http.get<any[]>(`${this.baseApiUrl}/risques/view`, { headers }).pipe(
      catchError(err => {
        console.error("Error fetching risques", err);
        return EMPTY;
      })      
    )
  }
}
