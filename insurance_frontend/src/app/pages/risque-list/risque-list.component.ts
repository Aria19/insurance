import { Component, OnInit, ViewChild } from '@angular/core';
import { Risque } from 'src/app/models/Risque';
import { RisqueService } from 'src/app/services/apiServices/risqueService/risque.service';
import { ModalComponent } from 'src/app/shared/modal/modal.component';

@Component({
  selector: 'app-risque-list',
  templateUrl: './risque-list.component.html',
  styleUrls: ['./risque-list.component.css']
})
export class RisqueListComponent implements OnInit {
  risques: Risque[] = [];
  role: string | null = '';

  toastMessage = '';
  toastType = 'success';

  @ViewChild(ModalComponent) deleteModal!: ModalComponent;
  private risqueIdToDelete: number | null = null;

  constructor(private risqueService: RisqueService) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    
    this.loadRisqus();
  }

  loadRisqus(){
    this.risqueService.getAllRisques().subscribe({
      next: (data) => {
        console.log('Risks fetched from service:', data);
        this.risques = data;
      },
      error: (err) => console.error('Error fetching risques:', err)
    });
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }

  openDeleteModal(idRisque: number): void {
    this.risqueIdToDelete = idRisque;
    this.deleteModal.open();
  }

  deleteUser(): void {
    if (this.risqueIdToDelete !== null) {
      this.risqueService.deleteRisque(this.risqueIdToDelete).subscribe({
        next: () => {
          this.showToast('Utilisateur supprimé avec succès.', 'success');
          this.loadRisqus();
        },
        error: () => this.showToast("Erreur lors de la suppression de l'utilisateur.", 'danger')
      });
    }
  }

  showToast(message: string, type: 'success' | 'danger') {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => this.toastMessage = '', 5000);
  }

  reloadUsers() {
    this.loadRisqus();
  }
}
