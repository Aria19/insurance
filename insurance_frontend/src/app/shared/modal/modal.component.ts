import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent {
  isVisible = false;
  @Output() confirmAction = new EventEmitter<boolean>();

  open() {
    this.isVisible = true;
  }

  close() {
    this.isVisible = false;
  }

  confirm() {
    this.confirmAction.emit(true);
    this.close();
  }

  cancel() {
    this.confirmAction.emit(false);
    this.close();
  }
}
