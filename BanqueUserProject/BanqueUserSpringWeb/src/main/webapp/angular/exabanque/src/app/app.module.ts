import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import {AgGridModule} from 'ag-grid-angular/main';

import { AppComponent } from './app.component';
import { PocComponent } from './form/test/poc/poc.component';
import { UserbanquelistComponent } from './taglist/users/userbanquelist/userbanquelist.component';
import { SearchbanqueaccountuserComponent } from './component/banque/searchbanqueaccountuser/searchbanqueaccountuser.component';


@NgModule({
  declarations: [
    AppComponent,
    PocComponent,
    UserbanquelistComponent,
    SearchbanqueaccountuserComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AgGridModule.withComponents([])
    
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class AppModule { }
