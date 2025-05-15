import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Contact } from 'src/app/models/Contact';
import { ContactService } from 'src/app/services/apiServices/contactService/contact.service';

@Component({
  selector: 'app-update-contact',
  templateUrl: './update-contact.component.html',
  styleUrls: ['./update-contact.component.css']
})
export class UpdateContactComponent implements OnInit {
  contactId!: number;
  contact: Contact = {
    idContact: 0,
    email: '',
    telephone: '',
    motDePasse: '',
    assure: '',
    societe: '',
    msh: '',
    cin: '',
    carteSejour: '',
    passeport: '',
    matriculeFiscale: ''
  };

  toastMessage = '';
  toastType = 'success';

  constructor(
    private route: ActivatedRoute,
    private contactService: ContactService
  ) {}

  ngOnInit(): void {
    this.contactId = Number(this.route.snapshot.paramMap.get('id'));
    this.getContactDetails();
  }

  getContactDetails(): void {
    this.contactService.getContacts().subscribe({
      next: (contacts) => {
        const found = contacts.find(c => c.idContact === this.contactId);
        if (found) {
          this.contact = found;
          console.log("Loaded contact: ", this.contact);
        } else {
          console.warn("No contact found with ID:", this.contactId);
        }
      },
      error: (err) => {
        console.error("Error fetching contacts:", err);
      }
    });
  }

  updateContact(): void {
    if (!this.contactId) {
      console.error('Missing contact ID.');
      return;
    }

    this.contactService.updateContact(this.contactId, this.contact).subscribe({
      next: (updated) => {
        console.log('Contact updated:', updated);
        this.showToast('Contact modifié avec succès.', 'success');
        // Optionally refresh the data or navigate away
      },
      error: (err) => {
        console.error('Error updating contact:', err);
        this.showToast("Erreur lors de la modification.", 'danger');
      }
    });
  }

  showToast(message: string, type: 'success' | 'danger') {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => this.toastMessage = '', 5000);
  }
}
