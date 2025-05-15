import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ContractService } from 'src/app/services/apiServices/contractService/contract.service';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RisqueService } from 'src/app/services/apiServices/risqueService/risque.service';
import Toast from 'bootstrap/js/dist/toast';

@Component({
  selector: 'app-contract-form',
  templateUrl: './contract-form.component.html',
  styleUrls: ['./contract-form.component.css']
})
export class ContractFormComponent implements OnInit {
  contractForm!: FormGroup;
  idContact!: number;
  risques: any[] = [];

  @ViewChild('toastElement', { static: false }) toastEl!: ElementRef;
  toastMessage: string = '';
  toastType: 'success' | 'error' = 'success';

  constructor(
    private fb: FormBuilder,
    private contractService: ContractService,
    private risqueService: RisqueService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.idContact = +this.route.snapshot.paramMap.get('id')!;
    this.initForm();
    this.risqueService.getAllRisques().subscribe({
      next: (data) => this.risques = data,
      error: (err) => console.error('Error fetching risques:', err)
    });
  }

  initForm(): void {
    this.contractForm = this.fb.group({
      contracts: this.fb.array([])
    });
    this.addContract(); // Add one contract by default
  }

  get contracts(): FormArray {
    return this.contractForm.get('contracts') as FormArray;
  }

  addContract(): void {
    const contractGroup = this.fb.group({
      codeRisque: [null, Validators.required],
      nature: [''],
      dateEffet: [''],
      dateEcheance: [''],
      mois: [''],
      dureeContrat: [''],
      modePayement: [''],
      nombreCheque: [''],
      numeroCheque: [''],
      dateDuCheque: [''],
      primeNette: [''],
      prime: [''],
      commission: [''],
      remarques: [''],
      transactions: this.fb.array([this.createTransaction()])
    });

    this.contracts.push(contractGroup);
  }

  createTransaction(): FormGroup {
    return this.fb.group({
      date: [''],
      montant: [''],
      terme: [''],
      modePayement: [''],
      nt: [''],
      bvBanque: [''],
      bvPortail: [''],
      remarque: ['']
    });
  }

  getTransactions(index: number): FormArray {
    return this.contracts.at(index).get('transactions') as FormArray;
  }

  addTransaction(contractIndex: number): void {
    this.getTransactions(contractIndex).push(this.createTransaction());
  }

  onSubmit(): void {
    if (this.contractForm.invalid) {
      this.contractForm.markAllAsTouched();
      return;
    }

    const contractsData = this.contractForm.value.contracts;
    contractsData.forEach((contract: any) => {
      this.contractService.addContractToContact(this.idContact, contract).subscribe({
        next: () => {
          this.toastType = 'success';
          this.toastMessage = 'Contrat ajouté avec succès !';
          this.showToast();
        },
        error: (err) => {
          this.toastType = 'error';
          this.toastMessage = 'Erreur lors de l\'ajout du contrat.';
          this.showToast();
          console.error('Error adding contract:', err);
        }
      });
    });
  }

  showToast(): void {
    const toast = new Toast(this.toastEl.nativeElement);
    toast.show();
  }

  goBack(): void {
    this.location.back();
  }

  /* goBack(): void {
    console.log('Navigating to contact ID:', this.idContact);
    this.router.navigate([`/dashboard/contacts/contact-details`, this.idContact]);
  }  */
}
