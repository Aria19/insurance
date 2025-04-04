import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContractService } from 'src/app/services/apiServices/contractService/contract.service';

@Component({
  selector: 'app-update-contract',
  templateUrl: './update-contract.component.html',
  styleUrls: ['./update-contract.component.css']
})
export class UpdateContractComponent implements OnInit {
  contract: any = {}; 
  contractId!: number;

  constructor(
    private route: ActivatedRoute,
    private contractService: ContractService,
    private router: Router
  ) {}

  ngOnInit() {
    this.contractId = Number(this.route.snapshot.paramMap.get('id')); // Get ID from URL
    this.getContractDetails();
  }

  getContractDetails() {
    this.contractService.getContracts().subscribe({
      next: (contracts) => {
        this.contract = contracts.find(c => c.id === this.contractId);
      },
      error: (error) => {
        console.error('Error fetching contract details', error);
      }
    });
  }
  
  updateContract() {
    if (!this.contractId) {
      console.error('No contract ID provided!');
      return;
    }
  
    this.contractService.updateContract(this.contractId, this.contract)
      .subscribe({
        next: (response) => {
          console.log('Update successful:', response);
          window.location.reload();
        },
        error: (err) => {
          console.error('Error updating contract:', err);
          alert('Something went wrong while updating the contract.');
        }
      });
  }
  

  goBack() {
    this.router.navigate(['/contracts']);
  }
  
}
