import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ContractService } from 'src/app/services/apiServices/contractService/contract.service';
import { Location } from '@angular/common';
import { Banque } from 'src/app/models/Banque';
import { CreateProductionDTO } from 'src/app/models/create-production.dto';
import { CreateBanqueDTO } from 'src/app/models/create-banque.dto';
import { BanqueService } from 'src/app/services/apiServices/banqueService/banque.service';
import { forkJoin, map, switchMap } from 'rxjs';

export interface ProductionWithTransactions {
  production: CreateProductionDTO;
  transactions: Banque[];
}


@Component({
  selector: 'app-contact-details',
  templateUrl: './contact-details.component.html',
  styleUrls: ['./contact-details.component.css']
})
export class ContactDetailsComponent implements OnInit {
  productionsWithTransactions: ProductionWithTransactions[] = [];
  contactId: number | null = null;

  constructor(
    private banqueService: BanqueService,
    private contractService: ContractService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.contactId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.contactId) {
      this.getContractsByContactId(this.contactId);
    }
  }

  getContractsByContactId(contactId: number): void {
    this.contractService.getContractsByContactId(contactId).pipe(
      switchMap((productions: CreateProductionDTO[]) => {
        const fetches = productions.map(prod =>
          this.banqueService.getTransactionsByContractId(prod.idProduction).pipe(
            map((transactions: Banque[] | undefined) => ({
              production: prod,
              transactions: transactions ?? []  // fallback to empty array if undefined
            }))
          )
        );
        return forkJoin(fetches);
      })
    ).subscribe({
      next: (result: ProductionWithTransactions[]) => {
        this.productionsWithTransactions = result;
      },
      error: (err) => {
        console.error('Failed to fetch contracts or transactions:', err);
      }
    });
  }
  
  goBack(): void {
    this.location.back();
  }
}