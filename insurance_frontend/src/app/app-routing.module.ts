import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './auth.guard';
import { ContractListComponent } from './pages/contract-list/contract-list.component';
import { DashboardLayoutComponent } from './layout/dashboard-layout/dashboard-layout.component';
import { UpdateContractComponent } from './pages/update-contract/update-contract.component';
import { ContactListComponent } from './pages/contact-list/contact-list.component';
import { ContactFormComponent } from './pages/contact-form/contact-form.component';
import { RisqueListComponent } from './pages/risque-list/risque-list.component';
import { UpdateContactComponent } from './pages/update-contact/update-contact.component';
import { ContactDetailsComponent } from './pages/contact-details/contact-details.component';
import { ContractFormComponent } from './pages/contract-form/contract-form.component';
import { UserListComponent } from './pages/user-list/user-list.component';
import { ProfileComponent } from './pages/profile/profile.component';

const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardLayoutComponent,
    children: [
      { path: '', component: DashboardComponent, canActivate: [authGuard] },
      { path: 'contracts', component: ContractListComponent, canActivate: [authGuard] },
      { path: 'contracts/add/:id', component: ContractFormComponent, canActivate: [authGuard] },
      { path: 'contracts/update-contract/:id', component: UpdateContractComponent, canActivate: [authGuard] },
      { path: 'contacts', component: ContactListComponent, canActivate: [authGuard] },
      { path: 'contacts/add', component: ContactFormComponent, canActivate: [authGuard] },
      { path: 'contacts/update-contact/:id', component: UpdateContactComponent, canActivate: [authGuard] },
      { path: 'contacts/contact-details/:id', component: ContactDetailsComponent, canActivate: [authGuard] },
      { path: 'risques', component: RisqueListComponent },
      { path: 'users', component: UserListComponent },
      { path: 'profile', component: ProfileComponent}

    ]
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // Redirect root to login
  { path: 'login', component: LoginComponent }, // Login route
  { path: '**', redirectTo: 'login' }, // Default route to login
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
