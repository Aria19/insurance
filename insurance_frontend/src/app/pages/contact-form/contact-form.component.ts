import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Risque } from 'src/app/models/Risque';
import { ContactService } from 'src/app/services/apiServices/contactService/contact.service';
import { RisqueService } from 'src/app/services/apiServices/risqueService/risque.service';

@Component({
  selector: 'app-contact-form',
  templateUrl: './contact-form.component.html'
})
export class ContactFormComponent implements OnInit {
  contactForm: FormGroup;
  risques: Risque[] = [];

  constructor(private fb: FormBuilder,
    private contactService: ContactService,
    private risqueService: RisqueService) {

    this.contactForm = this.fb.group({
      assure: ['', Validators.required],
      societe: [''],
      telephone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      msh: [''],
      motDePasse: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Za-z])(?=.*[^A-Za-z0-9]).{8,}$/)
      ]],
      cin: [''],
      carteSejour: [''],
      passeport: [''],
      matriculeFiscale: [''],

      // Now transactions are inside contracts
      contracts: this.fb.array([this.createContract()])
    });
  }

  ngOnInit() {
    this.risqueService.getAllRisques().subscribe({
      next: (data) => this.risques = data,
      error: (err) => console.error('Error fetching risques:', err)
    });
  }

  get contracts(): FormArray {
    return this.contactForm.get('contracts') as FormArray;
  }

  createContract(): FormGroup {
    return this.fb.group({
      codeRisque: ['', Validators.required],
      nature: [''],
      dateEffet: [''],
      dateEcheance: [''],
      mois: [0],
      dureeContrat: [''],
      modePayement: [''],
      nombreCheque: [0],
      numeroCheque: [''],
      dateDuCheque: [''],
      primeNette: [0],
      prime: [0],
      commission: [0],
      remarques: [''],

      transactions: this.fb.array([this.createTransaction()])
    });
  }

  createTransaction(): FormGroup {
    return this.fb.group({
      date: [''],
      montant: [0],
      terme: [''],
      modePayement: [''],
      nt: [''],
      bvBanque: [''],
      bvPortail: [''],
      remarque: ['']
    });
  }

  addContract() {
    this.contracts.push(this.createContract());
  }

  removeContract(index: number) {
    this.contracts.removeAt(index);
  }

  addTransaction(contractIndex: number) {
    const transactions = this.getTransactions(contractIndex);
    transactions.push(this.createTransaction());
  }

  removeTransaction(contractIndex: number, transactionIndex: number) {
    const transactions = this.getTransactions(contractIndex);
    transactions.removeAt(transactionIndex);
  }

  getTransactions(contractIndex: number): FormArray {
    return this.contracts.at(contractIndex).get('transactions') as FormArray;
  }

  submitForm() {
    console.log('Form submitted:', JSON.stringify(this.contactForm.value, null, 2));


    if (this.contactForm.valid) {
      this.contactService.createContact(this.contactForm.value).subscribe({
        next: (res) => alert('Contact added successfully!'),
        error: (err) => console.error('Error adding contact:', err)
      });
    } else {
      console.error('Form is invalid');
      this.contactForm.markAllAsTouched();
    }
  }

}
