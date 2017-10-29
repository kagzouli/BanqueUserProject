import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

import {ColumnApi, GridApi, GridOptions} from "ag-grid/main";

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
  
  listAccountOperation: ExaAccountOperation[] = [];
  
  private gridOptions: GridOptions;
  public columnDefs: any[];


  constructor(private fb: FormBuilder, private banqueService: BanqueService) {
       this.rForm = fb.group({
       'identifierUser' : [null, Validators.compose([Validators.required])],
      'beginDate' : [null /*, Validators.compose([Validators.]) */],
      'endDate' : [null /*,  Validators.compose([Validators.required])*/],
    });
     this.gridOptions = <GridOptions>{};
    
    this.columnDefs = this.createColumnDefs();
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
           }
         );

    }else {
         window.alert('There is a mistake in your input.');
         // Invalid data on form - we reset.
         this.rForm.reset();
     }
  }
  
  
    private createColumnDefs() {
        const columnDefs = [
            {
                headerName: 'Label operation',
                width: 150,
                field: 'label'
            },
            {
                headerName: 'Amount',
                width: 100,
                field: 'amount'
            },
            {
                headerName: 'operation Type',
                width: 100,
                field: 'operationType'
            },
            {
                headerName: 'operation Date',
                width: 100,
                field: 'operationDate'
            }
        ];

        return columnDefs;
    }
}
