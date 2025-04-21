import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContactService } from 'src/app/services/apiServices/contactService/contact.service';

@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.css']
})
export class ContactListComponent implements OnInit {

  contacts: any[] = [];
  page: number = 1;

  constructor(private contactService: ContactService, private router: Router){}

  ngOnInit(): void {
    this.getContacts();
  }

  getContacts(){
    this.contactService.getContacts().subscribe({
      next: (data) => { 
        console.log('Contacts fetched from service:', data);
        this.contacts = data; 
      },
      error: (err) => { console.error('Error fetching contacts:', err); }
    })
  }

  deleteContact(contractId: number) {
    if (confirm('Are you sure you want to delete this contract?')) {
      this.contactService.deleteContact(contractId).subscribe({
        next: () => {
          alert('Contact deleted successfully!');
        },
        error: (err) => {
          console.error('Error deleting contract:', err);
        }
      });
      window.location.reload();
    }
  }

  toggleAllSelection(event: any) {
    const isChecked = event.target.checked;
    this.contacts.forEach(contact => contact.selected = isChecked);
  }
  
  deleteSelectedContacts() {
    const selectedIds = this.contacts
      .filter(contact => contact.selected)
      .map(contact => contact.idContact);
  
    console.log("Selected Contact IDs:", selectedIds); // Debug
  
    if (selectedIds.length === 0) {
      alert("No contacts selected.");
      return;
    }
  
    if (confirm("Are you sure you want to delete the selected contacts?")) {
      this.contactService.deleteMultipleContacts(selectedIds).subscribe({
        next: () => {
          console.log("Contacts deleted successfully");
          // Re-fetch the updated contact list
          this.getContacts();
        },
        error: (err) => {
          console.error("Error deleting contacts:", err);
        }
      });
    }
  }  
  
  goToContactDetails(id: number): void {
    this.router.navigate(['/dashboard/contacts/contact-details', id]);
  }

}
