import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {MatTableModule} from '@angular/material';


import { AppComponent } from './app.component';
import { CreditBanqueAccountUser } from './component/banque/creditbanqueaccountuser/creditbanqueaccountuser.component';
import { UserbanquelistComponent } from './taglist/users/userbanquelist/userbanquelist.component';
import { SearchbanqueaccountuserComponent } from './component/banque/searchbanqueaccountuser/searchbanqueaccountuser.component';

import { AppRoutingModule, routingComponents } from './app.routing';
import { DebitbanqueaccountuserComponent } from './component/banque/debitbanqueaccountuser/debitbanqueaccountuser.component';

@NgModule({
  declarations: [
    AppComponent,
    UserbanquelistComponent,
    routingComponents,
    DebitbanqueaccountuserComponent 
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatTableModule,
    AppRoutingModule 
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class AppModule { }
