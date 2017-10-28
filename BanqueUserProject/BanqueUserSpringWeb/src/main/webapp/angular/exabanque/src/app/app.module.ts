import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { PocComponent } from './form/test/poc/poc.component';

import { BanqueService } from './services/banque/banque.service';


@NgModule({
  declarations: [
    AppComponent,
    PocComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [BanqueService],
  bootstrap: [AppComponent]
})
export class AppModule { }
