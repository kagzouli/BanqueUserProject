import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { BanqueService } from '../../../services/banque/banque.service';

@Component({
  selector: 'app-poc',
  templateUrl: './poc.component.html',
  styleUrls: ['./poc.component.css']
})
export class PocComponent implements OnInit {
  rForm: FormGroup;
  post:any;
  identifierUser: string = '';
  labelOperation: string = '';
  amount: number = 0;
  banqueService: BanqueService;

  constructor(private fb :FormBuilder, private _banqueService : BanqueService) {
    this.rForm = fb.group({
      'identifierUser' : [null, Validators.compose([Validators.required,Validators.maxLength(32)])],
      'labelOperation' : [null, Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(150)])],
      'amount' : [null,  Validators.compose([Validators.required, Validators.pattern('^\\d+\\.?\\d{0,2}$')])]
    });
    this.banqueService = _banqueService;
    
   }
  
  /**
   * Credit account for the user specified.
   * 
   */
   creditAccountController(accountOperation) {
    this.identifierUser = accountOperation.identifierUser;
    this.labelOperation = accountOperation.labelOperation;
    this.amount = accountOperation.amount;
         
    // Call the service crediting bank
    this.banqueService.creditAccountNumber(accountOperation.identifierUser , accountOperation.labelOperation, accountOperation.amount);
  }
  
  ngOnInit() {
  }
  
  changeIdentifierUser(oldvalue, newvalue){
    console.log("Old value : "+ oldvalue + "/ new value : "+ newvalue);
  }

  
}
