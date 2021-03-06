import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from "@angular/router";


import {AccountNumberOperation} from '../../../services/banque/accountnumberoperation';
import {JsonResult} from '../../../services/jsonresult';


import { BanqueService } from '../../../services/banque/banque.service';


@Component({
  selector: 'app-debitbanqueaccountuser',
  templateUrl: './debitbanqueaccountuser.component.html',
  styleUrls: ['./debitbanqueaccountuser.component.css'],
  providers: [BanqueService]
})
export class DebitbanqueaccountuserComponent implements OnInit {

  rForm: FormGroup;
  initUserCode: string = '';
  launchAction: boolean = false;


  constructor(private fb: FormBuilder, private banqueService: BanqueService, private parentRoute: ActivatedRoute, private router: Router) { 
    this.parentRoute.params.subscribe(params => {      
        this.initUserCode = params['userCodeSelected']; 
    });
      
    this.rForm = fb.group({
      'identifierUser' : [this.initUserCode, Validators.compose([Validators.required])],
      'labelOperation' : [null, Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(150)])],
      'amount' : [null,  Validators.compose([Validators.required, Validators.pattern('^\\d+\\.?\\d{0,2}$')])]
    });
    
  }

  ngOnInit() {
  }

  disableButton(invalidform : boolean){
    return invalidform || this.launchAction; 
  }



  /**
   * Debit account for the user specified.
   *
   */
  debitAccountController(accountOperation) {
    
        // Fill the account number operation
        const accountNumberOperation: AccountNumberOperation = new AccountNumberOperation(accountOperation.identifierUser, accountOperation.labelOperation, accountOperation.amount);
    
        // Call the service crediting bank
         if (this.rForm.valid) {
           //Before the credit disable the button.
           
             // Method of callback to credit account number
             this.launchAction = true;
             this.banqueService.debitAccountNumber(accountOperation,
             (jsonResult: JsonResult) => {
                 const success = jsonResult.success;
                 if (success) {
                    window.alert('The account of the user has been debit with success');
                    this.router.navigate(['/operation/searchbanqueaccountuser',{userCodeSelected: accountNumberOperation.identifierUser}]);               
                  }else {
                    let messageError = jsonResult.errors[0];
                    window.alert('Error --> ' + messageError);
                 }
                 this.launchAction = false;
    
               }
             );
             
    
        }else {
             // Invalid data on form - we reset.
             this.rForm.reset();
         }
      }

}
