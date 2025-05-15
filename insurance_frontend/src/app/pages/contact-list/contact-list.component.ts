import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ContactService } from 'src/app/services/apiServices/contactService/contact.service';
import { ModalComponent } from 'src/app/shared/modal/modal.component';

@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.css']
})
export class ContactListComponent implements OnInit {
  @ViewChild(ModalComponent) deleteModal!: ModalComponent;

  private contactIdToDelete: number | null = null;
  contacts: any[] = [];
  page: number = 1;

  selectedFile: File | null = null;
  importMessage: string = '';

  toastMessage = '';
  toastType: 'success' | 'danger' = 'success';

  searchForm = {
    assure: '',
    msh: '',
    societe: '',
    codeRisque: null as number | null,
    numeroContrat: ''
  };

  constructor(private contactService: ContactService, private router: Router) { }

  ngOnInit(): void {
    this.getContacts();
  }

  getContacts(): void {
    this.contactService.getContacts().subscribe({
      next: (data) => {
        this.contacts = data;
      },
      error: (err) => {
        console.error('Error fetching contacts:', err);
      }
    });
  }

  confirmAction: () => void = () => { };

  openDeleteModal(contactId: number): void {
    this.contactIdToDelete = contactId;
    this.confirmAction = () => this.confirmDelete();
    this.deleteModal.open();
  }

  openBulkDeleteModal(): void {
    this.confirmAction = () => this.deleteSelectedContacts();
    this.deleteModal.open();
  }

  onModalConfirm(): void {
    if (this.confirmAction) this.confirmAction();
  }

  confirmDelete(): void {
    if (this.contactIdToDelete !== null) {
      this.contactService.deleteContact(this.contactIdToDelete).subscribe({
        next: () => {
          this.getContacts();
          this.showToast('Contrat supprimé avec succès.', 'success');
        },
        error: () => {
          this.showToast("Erreur lors de la suppression.", 'danger');
        }
      });
    }
  }

  showToast(message: string, type: 'success' | 'danger'): void {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => this.toastMessage = '', 5000);
  }

  toggleAllSelection(event: any): void {
    const isChecked = event.target.checked;
    this.contacts.forEach(contact => contact.selected = isChecked);
  }

  deleteSelectedContacts(): void {
    const selectedIds = this.contacts.filter(c => c.selected).map(c => c.idContact);
    if (selectedIds.length === 0) {
      this.showToast("Aucun contact sélectionné.", 'danger');
      return;
    }
    this.contactService.deleteMultipleContacts(selectedIds).subscribe({
      next: () => {
        this.getContacts();
        this.showToast('Contact(s) supprimé(s) avec succès.', 'success');
      },
      error: () => {
        this.showToast("Erreur lors de la suppression.", 'danger');
      }
    });
  }

  goToContactDetails(id: number): void {
    this.router.navigate(['/dashboard/contacts/contact-details', id]);
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.selectedFile = input.files[0];
    }
  }

  importExcel(): void {
    if (!this.selectedFile) return;
    this.contactService.importExcel(this.selectedFile).subscribe({
      next: () => {
        this.getContacts();
        this.showToast('Fichier Excel importé avec succès.', 'success');
        this.selectedFile = null;
      },
      error: () => {
        this.showToast("Erreur lors de l’importation du fichier Excel.", 'danger');
      }
    });
  }

  searchContacts(): void {
    const hasInput = Object.values(this.searchForm).some(value => value !== '' && value !== null);
    if (!hasInput) {
      this.getContacts();
      return;
    }
    this.contactService.searchContacts(
      this.searchForm.assure,
      this.searchForm.msh,
      this.searchForm.societe,
      this.searchForm.codeRisque ?? undefined,
      this.searchForm.numeroContrat
    ).subscribe({
      next: (data) => {
        this.contacts = data;
        if (data.length === 0) {
          this.showToast("Aucun contact trouvé.", 'danger');
        }
      },
      error: () => {
        this.showToast("Erreur lors de la recherche.", 'danger');
      }
    });
  }

  onAssureInput(event: any): void {
    this.searchForm.assure = event.target.value;
    this.searchContacts();
  }

  onMshInput(event: any): void {
    this.searchForm.msh = event.target.value;
    this.searchContacts();
  }

  onSocieteInput(event: any): void {
    this.searchForm.societe = event.target.value;
    this.searchContacts();
  }

  onCodeRisqueChange(event: any): void {
    this.searchForm.codeRisque = event.target.value ? +event.target.value : null;
    this.searchContacts();
  }

  onNumeroContratInput(event: any): void {
    this.searchForm.numeroContrat = event.target.value;
    this.searchContacts();
  }

  exportContacts(): void {
    this.contactService.exportContacts(
      this.searchForm.assure,
      this.searchForm.msh,
      this.searchForm.societe,
      this.searchForm.codeRisque,
      this.searchForm.numeroContrat
    ).subscribe({
      next: (response: Blob) => {
        const blob = new Blob([response], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'Contacts.xlsx';
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => {
        this.showToast("Erreur lors de l'exportation.", 'danger');
      }
    });
  }
}

