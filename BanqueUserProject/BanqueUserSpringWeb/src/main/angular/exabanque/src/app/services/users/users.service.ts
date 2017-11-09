import { Injectable } from '@angular/core';

import { HttpClient , HttpHeaders} from '@angular/common/http';

import {UserLightBean} from './userlightbean';




@Injectable()
export class UsersService {

  // contextUserServiceUrl = 'http://localhost:25000';

  contextUserServiceUrl = 'http://localhost:12090';
  
  authorizationstring = 'Basic YmFucXVlOmJhTnFVZTM1Iw==';

  constructor( private http: HttpClient) { }

  /**
   * Method to retrieve all bank users.<br/>
   *
   */
  getAllBanqueUsers(callback: (userLightBean: UserLightBean[]) => void) {

     const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded').set('Authorization',this.authorizationstring);
     this.http.get<UserLightBean[]>(this.contextUserServiceUrl + '/userslist' , {headers: headers, withCredentials: true})
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
