import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import {AccountNumberOperation} from '../../../services/banque/accountnumberoperation';

import { BanqueService } from '../../../services/banque/banque.service';

@Component({
  selector: 'app-poc',
  templateUrl: './poc.component.html',
  styleUrls: ['./poc.component.css'],
  providers: [BanqueService]
})
export class PocComponent implements OnInit {
  rForm: FormGroup;
  post: any;
  constructor(private fb: FormBuilder, private banqueService: BanqueService) {
    this.rForm = fb.group({
      'identifierUser' : [null, Validators.compose([Validators.required, Validators.maxLength(32)])],
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
         this.banqueService.creditAccountNumber(accountOperation);
    }else {
         // Invalid data on form - we reset.
         this.rForm.reset();
     }
  }

  ngOnInit() {
  }

  changeIdentifierUser(oldvalue, newvalue) {
    console.log('Old value : ' + oldvalue + '/ new value : ' + newvalue);
  }


}
