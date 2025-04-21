import { Injectable } from '@angular/core';
import { BaseService } from '../../baseService/base.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Banque } from 'src/app/models/Banque';

@Injectable({
  providedIn: 'root'
})
export class BanqueService extends BaseService {

  constructor(private http: HttpClient) {
      super();
  }

  getTransactionsByContractId(productionId: number): Observable<Banque[]> {
    const headers = this.getAuthHeaders();

    return this.http.get<Banque[]>(`${this.baseApiUrl}/Banques/production/${productionId}`, {headers});
  }
}

