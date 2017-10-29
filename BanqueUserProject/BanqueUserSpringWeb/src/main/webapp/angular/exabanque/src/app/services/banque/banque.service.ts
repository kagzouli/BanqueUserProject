import { Injectable } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';

import {AccountNumberOperation} from './accountnumberoperation';
import {JsonResult} from '../jsonresult';
import { HttpClient , HttpHeaders} from '@angular/common/http';


@Injectable()
export class BanqueService {

  constructor( private http: HttpClient) { }

  creditAccountNumber(accountNumberOperation: AccountNumberOperation, callback: (jsonResult: JsonResult) => void) {

    /*  this.http.get('http://localhost:25000/usersList')
      .map((res: Response) => res.json()).subscribe((data) => console.log(data)); */

     const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
     this.http.post<JsonResult>('http://localhost:25000/creditAccount', JSON.stringify(accountNumberOperation), {headers: headers})
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
