import { Injectable } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';

import {AccountNumberOperation} from './accountnumberoperation';
import { HttpClient , HttpHeaders} from '@angular/common/http';


@Injectable()
export class BanqueService {

  constructor( private http: HttpClient) { }

  creditAccountNumber(accountNumberOperation: AccountNumberOperation) {

    /*  this.http.get('http://localhost:25000/usersList')
      .map((res: Response) => res.json()).subscribe((data) => console.log(data)); */

     const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

     this.http.post('http://localhost:25000/creditAccount', JSON.stringify(accountNumberOperation), {headers: headers})
       .subscribe(
        res => {
          console.log(res);
        },
        err => {
          console.log('Error occured --> ' + err);
        }
      );
    /*   .map((response: Response) => {
            // login successful if there's a jwt token in the response
            const token = response.json() && response.json().token;
            console.log(response);
        });*/
     /* .catch((error: any) => {
        console.log(error);
      return Observable.throw(error.json()); }); */
   // this.http.post(endPoint,)

   // this.jsonp.put(endPoint, { search: params })
     //         .map(response => <Animal[]> response.json().petfinder.pets.pet);


  }

}
