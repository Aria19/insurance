import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RisqueFormModalComponent } from './risque-form-modal.component';

describe('RisqueFormModalComponent', () => {
  let component: RisqueFormModalComponent;
  let fixture: ComponentFixture<RisqueFormModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RisqueFormModalComponent]
    });
    fixture = TestBed.createComponent(RisqueFormModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
