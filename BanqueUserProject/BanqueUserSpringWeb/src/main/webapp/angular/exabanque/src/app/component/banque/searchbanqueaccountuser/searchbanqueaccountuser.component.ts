import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from "@angular/router";

import {ExaAccountOperation} from '../../../services/banque/exaaccountoperation';
import {SearchAccountOperation} from '../../../services/banque/searchaccountoperationparam';


import { BanqueService } from '../../../services/banque/banque.service';

import {DataSource} from '@angular/cdk/collections';
import {MatPaginator} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/observable/merge';
import 'rxjs/add/operator/map';


@Component({
  selector: 'app-searchbanqueaccountuser',
  templateUrl: './searchbanqueaccountuser.component.html',
  styleUrls: ['./searchbanqueaccountuser.component.css'],
  providers: [BanqueService]
})
export class SearchbanqueaccountuserComponent implements OnInit {

  rForm: FormGroup;
  
  displayedColumns = ['operationDate', 'label', 'operationType', 'amount'];
   
  listAccountOperation: ExaAccountOperation[] = [];
  
  dataSource = new ExabanqueDataSource(this);
  

  constructor(private fb: FormBuilder, private banqueService: BanqueService, private changeDetectorRefs: ChangeDetectorRef, private router: Router) {
       this.rForm = fb.group({
       'identifierUser' : [null, Validators.compose([Validators.required])],
      'beginDate' : [null /*, Validators.compose([Validators.]) */],
      'endDate' : [null /*,  Validators.compose([Validators.required])*/],
    });
   }

  ngOnInit() {
  }

  searchaccountoperation(form) {
    
      // Convert begin date to ISO8601
      let beginDateIso8601 : Date;
      let endDateIso8601 : Date;
      if (form.beginDate != null){
         beginDateIso8601 =  new Date(new Date(form.beginDate).toISOString());
      }
    
      if (form.endDate != null){
        endDateIso8601 =  new Date(new Date(form.endDate).toISOString());
      }
       
    
      const searchAccountOperation: SearchAccountOperation = new SearchAccountOperation(form.identifierUser, beginDateIso8601, endDateIso8601);
    
        // Call the search accountoperations if valid
      if (this.rForm.valid) {
         // Method of callback to credit account number
         this.banqueService.findAllAccountOperation(searchAccountOperation,
         (listAccountOperation: ExaAccountOperation[]) => {
             this.listAccountOperation = listAccountOperation;
             this.dataSource = new ExabanqueDataSource(this);
             this.changeDetectorRefs.detectChanges();
           }
         );

    }else {
         window.alert('There is a mistake in your input.');
         // Invalid data on form - we reset.
         this.rForm.reset();
     }
  } 
  
  openCreditAccount(event) {
    console.log('event : ' + event);
    this.router.navigate(['/operation/creditbanqueaccountuser']);
  }
 }

/**
 * Data source to provide what data should be rendered in the table. The observable provided
 * in connect should emit exactly the data that should be rendered by the table. If the data is
 * altered, the observable should emit that new set of data on the stream. In our case here,
 * we return a stream that contains only one set of data that doesn't change.
 */
export class ExabanqueDataSource extends DataSource<any> {
  
   constructor(private _searchbanqueaccount: SearchbanqueaccountuserComponent) {
    super();
  }
  
  
  /** Connect function called by the table to retrieve one stream containing the data to render. */
  connect(): Observable<ExaAccountOperation[]> {
    return Observable.of(this._searchbanqueaccount.listAccountOperation);
  }

  disconnect() {}
}
