import { Injectable } from '@angular/core';
import { Http} from '@angular/http';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';

import {AccountNumberOperation} from './accountnumberoperation';


@Injectable()
export class BanqueService {

  constructor( private http: Http) { }

  creditAccountNumber(accountNumberOperation: AccountNumberOperation) {

    const endPoint = '/creditAccount';

    this.http.post('/creditAccount', accountNumberOperation)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
   // this.http.post(endPoint,)

   // this.jsonp.put(endPoint, { search: params })
     //         .map(response => <Animal[]> response.json().petfinder.pets.pet);


  }

}
