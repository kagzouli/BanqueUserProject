import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from "@angular/router";
import {$WebSocket, WebSocketSendMode } from 'angular2-websocket/angular2-websocket';


import {ExaAccountOperation} from '../../../services/banque/exaaccountoperation';
import {SearchAccountOperation} from '../../../services/banque/searchaccountoperationparam';


import { BanqueService } from '../../../services/banque/banque.service';

import {JsonResult} from '../../../services/jsonresult';

import {ExabanqueDataSource} from '../../../datasource/exabanquedatasource';


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
     
  dataSource = new ExabanqueDataSource();

  initUserCode : string;

  launchAction : boolean = false;

  balanceUser : number;

  socket : $WebSocket =  new $WebSocket('ws://localhost:14090/sendDateToDisplay/websocket');
  
  messageDisplay : string = '';

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

   /**
    * Init.
    */
  ngOnInit() {

     this.socket.getDataStream().subscribe(
      (msg)=> {
       console.log("next", msg.data);
       this.messageDisplay = msg.data;
    },
    (msg)=> {
        console.log("error", msg);
    },
    ()=> {
        console.log("complete");
    }
    );

    

  }

  /**
   * Destroy.
   * 
   */
  ngOnDestroy(){
   // this.socket.close(true);    // close immediately
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
        endDateIso8601.setHours(23 , 59 , 59 , 59);
      }
       
    
      const searchAccountOperation: SearchAccountOperation = new SearchAccountOperation(form.identifierUser, beginDateIso8601, endDateIso8601);
    
        // Call the search accountoperations if valid
      if (this.rForm.valid) {
         this.launchAction = true;
         // Method of callback to credit account number
         this.banqueService.findAllAccountOperation(searchAccountOperation,
         (listAccountOperation: ExaAccountOperation[]) => {
             this.dataSource.updateValue(listAccountOperation);
             this.retrieveBalanceUser(form.identifierUser);
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

  retrieveBalanceUser (userIdentifier : string){
    this.banqueService.retrieveBalanceAccountUser(userIdentifier,
      (jsonResult : JsonResult) => {
          if (jsonResult.success){
             this.balanceUser = jsonResult.result;
          }
        }
      );
  }

  openDebitAccount(event) {
    let identifierUser = '';
    if (this.rForm.valid){
      identifierUser = this.rForm.value.identifierUser;
    }

    this.router.navigate(['/operation/debitbanqueaccountuser',{userCodeSelected: identifierUser}]);
  }
 }

