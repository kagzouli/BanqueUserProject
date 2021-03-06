import { Injectable } from '@angular/core';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';
import {AccountNumberOperation} from './accountnumberoperation';
import {ExaAccountOperation} from './exaaccountoperation';
import {SearchAccountOperation} from './searchaccountoperationparam';


import {JsonResult} from '../jsonresult';
import { HttpClient , HttpHeaders , HttpParams} from '@angular/common/http';


@Injectable()
export class BanqueService {
  
  
  contextUserServiceUrl = 'http://localhost:14090';

  authorizationstringmanager = 'Basic bWFuYWdlcjpiYU5rbWFOYWdlcjM1Iw==';
  authorizationstringcollaborator = 'Basic Y29sbGFib3JhdG9yOmNvbGxhQm9SYXRvcjM1Iw==';
  

  constructor( private http: HttpClient) { }

  /**
   * Method to credit an account for a user.
   * 
   */
  creditAccountNumber(accountNumberOperation: AccountNumberOperation, callback: (jsonResult: JsonResult) => void) {

    /*  this.http.get('http://localhost:25000/usersList')
      .map((res: Response) => res.json()).subscribe((data) => console.log(data)); */

     const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8').set('Authorization',this.authorizationstringmanager);
     this.http.post<JsonResult>(this.contextUserServiceUrl + '/creditAccount', JSON.stringify(accountNumberOperation), {headers: headers, withCredentials: true})
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
   * Method to credit an account for a user.
   * 
   */
  debitAccountNumber(accountNumberOperation: AccountNumberOperation, callback: (jsonResult: JsonResult) => void) {
    
         const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8').set('Authorization',this.authorizationstringmanager);
         this.http.post<JsonResult>(this.contextUserServiceUrl + '/debitAccount', JSON.stringify(accountNumberOperation), {headers: headers, withCredentials: true})
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
        const headers = new HttpHeaders().set('Content-Type', 'application/json').set('Authorization',this.authorizationstringcollaborator);
        this.http.post<ExaAccountOperation[]>(this.contextUserServiceUrl + '/listOperations', searchaccountOperationParam, {headers: headers , withCredentials: true})
       .subscribe(
        res => {
          callback(res);
        },
        err => {
          console.log('Error occured --> ' + err);
        }
      );
   }

   retrieveBalanceAccountUser(userCodeIdentifier : string, callback: (listAccountOperations: JsonResult) => void) {
    
    let params = new HttpParams();
    params = params.append('userIdentifier', userCodeIdentifier);
    
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded').set('Authorization',this.authorizationstringcollaborator);
    this.http.get<JsonResult>(this.contextUserServiceUrl + '/balanceAccountUser' , {params: params, headers: headers, withCredentials: true})
      .subscribe(
       res => {
         callback(res);
       },
       err => {
         // TODO : A gerer avec des redirections
         console.log('Error occured --> ' + err);
       }
     );
   }
 
 
}
