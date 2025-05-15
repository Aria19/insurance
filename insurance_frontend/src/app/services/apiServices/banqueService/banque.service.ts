import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Banque } from 'src/app/models/Banque';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BanqueService {

  constructor(private http: HttpClient) {
  }

  getTransactionsByContractId(productionId: number): Observable<Banque[]> {

    return this.http.get<Banque[]>(`${environment.apiUrl}/Banques/production/${productionId}`);
  }
}

