import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './auth.guard';
import { ContractListComponent } from './pages/contract-list/contract-list.component';
import { DashboardLayoutComponent } from './layout/dashboard-layout/dashboard-layout.component';
import { UpdateContractComponent } from './pages/update-contract/update-contract.component';

const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardLayoutComponent,
    children: [
      { path: '', component: DashboardComponent, canActivate: [authGuard] }, // Default view inside dashboard
      { path: 'update-contract/:id', component: UpdateContractComponent, canActivate: [authGuard] },
      { path: 'contracts', component: ContractListComponent, canActivate: [authGuard] }
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
