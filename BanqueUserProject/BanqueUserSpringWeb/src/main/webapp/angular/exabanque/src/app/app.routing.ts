import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreditBanqueAccountUser } from './component/banque/creditbanqueaccountuser/creditbanqueaccountuser.component';
import { SearchbanqueaccountuserComponent } from './component/banque/searchbanqueaccountuser/searchbanqueaccountuser.component';



const routes: Routes = [
    { path: '',redirectTo: '/operation/searchbanqueaccountuser',  pathMatch: 'full' },
    { path: 'operation/searchbanqueaccountuser', component: SearchbanqueaccountuserComponent },
    { path: 'operation/creditbanqueaccountuser', component: CreditBanqueAccountUser },
  ];
  
  @NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
  })
  export class AppRoutingModule { }
 
  export const routingComponents = [SearchbanqueaccountuserComponent, CreditBanqueAccountUser];