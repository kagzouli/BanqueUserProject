import { Injectable } from '@angular/core';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';

import {AccountNumberOperation} from './accountnumberoperation';
import {ExaAccountOperation} from './exaaccountoperation';
import {SearchAccountOperation} from './searchaccountoperationparam';


import {JsonResult} from '../jsonresult';
import { HttpClient , HttpHeaders} from '@angular/common/http';


@Injectable()
export class BanqueService {
  
  contextUserServiceUrl = 'http://localhost:25000';  

  constructor( private http: HttpClient) { }

  /**
   * Method to credit an account for a user.
   * 
   */
  creditAccountNumber(accountNumberOperation: AccountNumberOperation, callback: (jsonResult: JsonResult) => void) {

    /*  this.http.get('http://localhost:25000/usersList')
      .map((res: Response) => res.json()).subscribe((data) => console.log(data)); */

     const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
     this.http.post<JsonResult>(this.contextUserServiceUrl + '/creditAccount', JSON.stringify(accountNumberOperation), {headers: headers})
       .subscribe(
        res => {
          callback(res);
        },
        err => {
          console.log('Error occured --> ' + err);
        }
      );
   }
  
  
   /**
   * Method to find all account operations.
   * 
   */  
   findAllAccountOperation(searchaccountOperationParam: SearchAccountOperation, callback: (listAccountOperations: ExaAccountOperation[]) => void){
        const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
        this.http.post<ExaAccountOperation[]>(this.contextUserServiceUrl + '/listOperations', JSON.stringify(searchaccountOperationParam), {headers: headers})
       .subscribe(
        res => {
          callback(res);
        },
        err => {
          console.log('Error occured --> ' + err);
        }
      );
   }
 
 
}
