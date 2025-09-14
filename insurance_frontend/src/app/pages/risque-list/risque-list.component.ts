import { Component, OnInit, ViewChild } from '@angular/core';
import { Risque } from 'src/app/models/Risque';
import { RisqueService } from 'src/app/services/apiServices/risqueService/risque.service';
import { ModalComponent } from 'src/app/shared/delete-modal/modal.component';

declare var bootstrap: any;

@Component({
  selector: 'app-risque-list',
  templateUrl: './risque-list.component.html',
  styleUrls: ['./risque-list.component.css']
})
export class RisqueListComponent implements OnInit {
  risques: Risque[] = [];
  role: string | null = '';
  toastMessage = '';
  toastType: 'success' | 'danger' = 'success';

  @ViewChild(ModalComponent) deleteModal!: ModalComponent;
  private risqueIdToDelete: number | null = null;

  formData: any = {};
  isEditing = false;

  constructor(private risqueService: RisqueService) { }

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    this.loadRisques();
  }

  loadRisques(): void {
    this.risqueService.getAllRisques().subscribe({
      next: (data) => {
        this.risques = data;
        console.log('Risques loaded:', data);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des risques', err);
        this.showToast('Erreur lors du chargement des risques.', 'danger');
      }
    });
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }

  openAddModal(): void {
    this.formData = { codeRisque: '', risqueName: '', commission: null };
    this.isEditing = false;
    this.showModal();
  }

  openEditModal(risque: Risque): void {
    this.formData = { ...risque };
    this.isEditing = true;
    this.showModal();
  }

  handleFormSubmit(data: any): void {
    if (!data.codeRisque || !data.risqueName || data.commission == null) {
      this.showToast('Tous les champs sont requis.', 'danger');
      return;
    }

    const action$ = this.isEditing
      ? this.risqueService.updateRisque(data.idRisque, data)
      : this.risqueService.addRisque(data);

    action$.subscribe({
      next: () => {
        const message = this.isEditing
          ? 'Risque modifié avec succès.'
          : 'Risque ajouté avec succès.';
        this.showToast(message, 'success');
        this.loadRisques();
        this.hideModal();
      },
      error: () => {
        const message = this.isEditing
          ? 'Erreur lors de la modification.'
          : 'Erreur lors de l’ajout.';
        this.showToast(message, 'danger');
      }
    });
  }

  openDeleteModal(idRisque: number): void {
    this.risqueIdToDelete = idRisque;
    this.deleteModal.open();
  }

  deleteUser(): void {
    if (this.risqueIdToDelete !== null) {
      this.risqueService.deleteRisque(this.risqueIdToDelete).subscribe({
        next: () => {
          this.showToast('Risque supprimé avec succès.', 'success');
          this.loadRisques();
        },
        error: () => this.showToast('Erreur lors de la suppression.', 'danger')
      });
    }
  }

  showToast(message: string, type: 'success' | 'danger'): void {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => (this.toastMessage = ''), 5000);
  }

  showModal(): void {
    const modal = new bootstrap.Modal(document.getElementById('risqueFormModal')!);
    modal.show();
  }

  hideModal(): void {
    const modal = bootstrap.Modal.getInstance(document.getElementById('risqueFormModal')!);
    modal?.hide();
  }

  closeModal(): void {
    this.hideModal();
  }

}
