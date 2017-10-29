import { Injectable } from '@angular/core';

import { HttpClient , HttpHeaders} from '@angular/common/http';

import {UserLightBean} from './userlightbean';




@Injectable()
export class UsersService {

  contextUserServiceUrl = 'http://localhost:25000';

  constructor( private http: HttpClient) { }

  /**
   * Method to retrieve all bank users.<br/>
   *
   */
  getAllBanqueUsers(callback: (userLightBean: UserLightBean[]) => void) {

     const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
     this.http.get<UserLightBean[]>(this.contextUserServiceUrl + '/usersList')
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
