import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-searchbanqueaccountuser',
  templateUrl: './searchbanqueaccountuser.component.html',
  styleUrls: ['./searchbanqueaccountuser.component.css']
})
export class SearchbanqueaccountuserComponent implements OnInit {

  rForm: FormGroup;


  constructor(private fb: FormBuilder) {
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

  }

}
