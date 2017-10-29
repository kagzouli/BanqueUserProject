import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

import {ExaAccountOperation} from '../../../services/banque/exaaccountoperation';
import {SearchAccountOperation} from '../../../services/banque/searchaccountoperationparam';

import { BanqueService } from '../../../services/banque/banque.service';

@Component({
  selector: 'app-searchbanqueaccountuser',
  templateUrl: './searchbanqueaccountuser.component.html',
  styleUrls: ['./searchbanqueaccountuser.component.css'],
  providers: [BanqueService]
})
export class SearchbanqueaccountuserComponent implements OnInit {

  rForm: FormGroup;
  
  listAccountOperation: ExaAccountOperation[];


  constructor(private fb: FormBuilder, private banqueService: BanqueService) {
       this.rForm = fb.group({
       'identifierUser' : [null, Validators.compose([Validators.required])],
      'beginDate' : [null /*, Validators.compose([Validators.]) */],
      'endDate' : [null /*,  Validators.compose([Validators.required])*/],
    });
   }

  ngOnInit() {
  }

  searchaccountoperation(form) {
      console.log('Identifier user : ' + form.identifierUser);
      console.log('Begin date : ' + form.beginDate);
      console.log('End date : ' + form.endDate);
    
      const searchAccountOperation: SearchAccountOperation = new SearchAccountOperation(form.identifierUser, form.beginDate, form.endDate);
    
        // Call the search accountoperations if valid
      if (this.rForm.valid) {
         // Method of callback to credit account number
         this.banqueService.findAllAccountOperation(searchAccountOperation,
         (listAccountOperation: ExaAccountOperation[]) => {
             this.listAccountOperation = listAccountOperation;
           }
         );

    }else {
         window.alert('There is a mistake in your input.');
         // Invalid data on form - we reset.
         this.rForm.reset();
     }
    
    

  }

}
