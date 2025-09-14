import { Component, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';

declare var bootstrap: any;

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html'
})
export class ModalComponent {
  @Input() title: string = '';
  @Input() message: string = '';
  @Output() confirm = new EventEmitter<void>();

  @ViewChild('modalElement') modalElement!: ElementRef;
  private modalInstance: any;

  // Show the modal programmatically
  open(): void {
    if (!this.modalInstance) {
      this.modalInstance = new bootstrap.Modal(this.modalElement.nativeElement);
    }
    this.modalInstance.show();
  }

  // Hide the modal programmatically
  close(): void {
    if (this.modalInstance) {
      this.modalInstance.hide();
    }
  }

  onConfirm(): void {
    this.confirm.emit();
    this.close(); // auto-close on confirm
  }
}
