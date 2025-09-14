import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-risque-form-modal',
  templateUrl: './risque-form-modal.component.html',
  styleUrls: ['./risque-form-modal.component.css']
})
export class RisqueFormModalComponent {

  @Input() formData: any = { codeRisque: null, risqueName: '', commission: null };
  @Input() isEditing: boolean = false;

  @Output() submit = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  onSubmit(form: NgForm) {
    if (form.invalid) {
      return;
    }

    this.submit.emit(this.formData);
  }

  onCancel() {
    this.cancel.emit();
  }

}
