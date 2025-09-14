import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ContractListComponent } from './pages/contract-list/contract-list.component';
import { DashboardLayoutComponent } from './layout/dashboard-layout/dashboard-layout.component';
import { ModalComponent } from './shared/delete-modal/modal.component';
import { UpdateContractComponent } from './pages/update-contract/update-contract.component';
import { ContactListComponent } from './pages/contact-list/contact-list.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ContactFormComponent } from './pages/contact-form/contact-form.component';
import { RisqueListComponent } from './pages/risque-list/risque-list.component';
import { UpdateContactComponent } from './pages/update-contact/update-contact.component';
import { ContactDetailsComponent } from './pages/contact-details/contact-details.component';
import { ContractFormComponent } from './pages/contract-form/contract-form.component';
import { UserListComponent } from './pages/user-list/user-list.component';
import { AuthInterceptorService } from './interceptors/auth-interceptor.service';
import { RisqueFormModalComponent } from './shared/risque-form-modal/risque-form-modal.component';
import { ProfileComponent } from './pages/profile/profile.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    ContractListComponent,
    DashboardLayoutComponent,
    ModalComponent,
    UpdateContractComponent,
    ContactListComponent,
    ContactFormComponent,
    RisqueListComponent,
    UpdateContactComponent,
    ContactDetailsComponent,
    ContractFormComponent,
    UserListComponent,
    RisqueFormModalComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxPaginationModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
