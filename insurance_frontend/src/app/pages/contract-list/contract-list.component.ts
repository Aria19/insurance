import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { ContractService } from 'src/app/services/apiServices/contractService/contract.service';
import { ModalComponent } from 'src/app/shared/modal/modal.component';

@Component({
  selector: 'app-contract-list',
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.css']
})
export class ContractListComponent implements OnInit {
  searchKeyword: string = '';
  risque: string = '';
  codeRisque: number | undefined = undefined;
  dateEffet: number | undefined = undefined;

  contracts: any[] = [];
  page: number = 1;
  
  role: string | null = '';

  constructor(private contractService: ContractService) { }

  ngOnInit(): void {
    this.loadContracts();
    this.role = localStorage.getItem('role');
  }

  loadContracts(): void {
    // Pass all the search criteria to the service
    this.contractService
      .searchContracts(this.searchKeyword, this.risque, this.codeRisque, this.dateEffet)
      .subscribe({
        next: (data) => {
          console.log('Fetched Contracts:', data);
          this.contracts = data;
        },
        error: (err) => {
          console.error('Error fetching contracts:', err);
        },
      });
  }

  onSearchInput(event: any): void {
    this.searchKeyword = event.target.value;
    this.loadContracts(); // Trigger search on every input
  }

  onRisqueChange(event: any): void {
    this.risque = event.target.value;
    this.loadContracts();
  }

  onCodeRisqueChange(event: any): void {
    this.codeRisque = event.target.value;
    this.loadContracts();
  }

  onDateEffetChange(event: any): void {
    this.dateEffet = event.target.value;
    this.loadContracts();
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }

  isAgent(): boolean {
    return this.role === 'AGENT';
  }

  exportContracts() {
    console.log("Exporting with filters:", this.searchKeyword, this.risque, this.codeRisque, this.dateEffet); // Debugging

    this.contractService.exportContracts(this.searchKeyword, this.risque, this.codeRisque, this.dateEffet)
      .subscribe({
        next: (response: Blob) => {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
          const url = window.URL.createObjectURL(blob);

          const a = document.createElement('a');
          a.href = url;
          a.download = 'Contracts.xlsx';
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
        },
        error: (error) => {
          console.error('Export failed:', error);
        }
      });
  }

  deleteContract(contractId: number) {
    if (confirm('Are you sure you want to delete this contract?')) {
      this.contractService.deleteContract(contractId).subscribe({
        next: () => {
          alert('Contract deleted successfully!');
        },
        error: (err) => {
          console.error('Error deleting contract:', err);
        }
      });
      window.location.reload();
    }
  }

}
