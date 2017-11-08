import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from "@angular/router";

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
  
  dataSource = new ExabanqueDataSource(this.listAccountOperation);

  initUserCode : string;

  launchAction : boolean = false;

  constructor(private fb: FormBuilder, private banqueService: BanqueService, private changeDetectorRefs: ChangeDetectorRef,private parentRoute: ActivatedRoute, private router: Router) {
      
      //Init value
      this.parentRoute.params.subscribe(params => {        
          this.initUserCode =  params['userCodeSelected'] != null ? params['userCodeSelected'] : ''; 
      });
        
    
      this.rForm = fb.group({
       'identifierUser' : [this.initUserCode, Validators.compose([Validators.required])],
      'beginDate' : [null /*, Validators.compose([Validators.]) */],
      'endDate' : [null /*,  Validators.compose([Validators.required])*/],
    });

    // Initialise the list.
    if (this.rForm.valid){
      this.searchaccountoperation(this.rForm.value);   
    }
   }

  ngOnInit() {
  }

  disableButton(invalidform : boolean){
    return invalidform || this.launchAction; 
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
         this.launchAction = true;
         // Method of callback to credit account number
         this.banqueService.findAllAccountOperation(searchAccountOperation,
         (listAccountOperation: ExaAccountOperation[]) => {
             this.listAccountOperation = listAccountOperation;
             this.dataSource = new ExabanqueDataSource(this.listAccountOperation);
             this.changeDetectorRefs.detectChanges();
             this.launchAction = false;
           }
         );

    }else {
         window.alert('There is a mistake in your input.');
         // Invalid data on form - we reset.
         this.rForm.reset();
         this.launchAction = false;
     }
  } 
  
  openCreditAccount(event) {
    let identifierUser = '';
    if (this.rForm.valid){
      identifierUser = this.rForm.value.identifierUser;
    }

    this.router.navigate(['/operation/creditbanqueaccountuser',{userCodeSelected: identifierUser}]);
  }

  openDebitAccount(event) {
    let identifierUser = '';
    if (this.rForm.valid){
      identifierUser = this.rForm.value.identifierUser;
    }

    this.router.navigate(['/operation/debitbanqueaccountuser',{userCodeSelected: identifierUser}]);
  }
 }

/**
 * Data source to provide what data should be rendered in the table. The observable provided
 * in connect should emit exactly the data that should be rendered by the table. If the data is
 * altered, the observable should emit that new set of data on the stream. In our case here,
 * we return a stream that contains only one set of data that doesn't change.
 */
export class ExabanqueDataSource extends DataSource<any> {
  
   constructor(private listAccountOperation: ExaAccountOperation[]) {
    super();
  }
  
  
  /** Connect function called by the table to retrieve one stream containing the data to render. */
  connect(): Observable<ExaAccountOperation[]> {
    return Observable.of(this.listAccountOperation);
  }

  disconnect() {}
}
