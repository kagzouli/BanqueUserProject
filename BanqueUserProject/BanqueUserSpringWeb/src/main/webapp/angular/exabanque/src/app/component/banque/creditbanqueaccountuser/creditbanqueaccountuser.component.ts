import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from "@angular/router";


import {AccountNumberOperation} from '../../../services/banque/accountnumberoperation';
import {JsonResult} from '../../../services/jsonresult';


import { BanqueService } from '../../../services/banque/banque.service';

@Component({
  selector: 'creditbanqueaccountuser',
  templateUrl: './creditbanqueaccountuser.component.html',
  styleUrls: ['./creditbanqueaccountuser.component.css'],
  providers: [BanqueService]
})
export class CreditBanqueAccountUser implements OnInit {
  rForm: FormGroup;
  identifierUserCste = 'identifierUser';
  post: any;
  constructor(private fb: FormBuilder, private banqueService: BanqueService, private router: Router) {
    this.rForm = fb.group({
      'identifierUser' : [null, Validators.compose([Validators.required])],
      'labelOperation' : [null, Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(150)])],
      'amount' : [null,  Validators.compose([Validators.required, Validators.pattern('^\\d+\\.?\\d{0,2}$')])]
    });

   }

  /**
   * Credit account for the user specified.
   *
   */
   creditAccountController(accountOperation) {

    // Fill the account number operation
    const accountNumberOperation: AccountNumberOperation = new AccountNumberOperation(accountOperation.identifierUser, accountOperation.labelOperation, accountOperation.amount);

    // Call the service crediting bank
     if (this.rForm.valid) {
       //Before the credit disable the button.
       
        this.rForm.disable();
         // Method of callback to credit account number
         this.banqueService.creditAccountNumber(accountOperation,
         (jsonResult: JsonResult) => {
             const success = jsonResult.success;
             if (success) {
                window.alert('The account of the user has been credit with success');
                this.router.navigate(['/']);               
              }else {
                window.alert('The application has face a technical error.');
             }

           }
         );

         //After finishing enable the form
         this.rForm.enable();
         

    }else {
         // Invalid data on form - we reset.
         this.rForm.reset();
     }
  }

  ngOnInit() {
  }


}
