import { Component, OnInit } from '@angular/core';

import { UsersService } from '../../../services/users/users.service';

import {UserLightBean} from '../../../services/users/userlightbean';

import {StringMapEntry} from '../../../services/stringmapentry';



@Component({
  selector: 'app-userbanquelist',
  templateUrl: './userbanquelist.component.html',
  styleUrls: ['./userbanquelist.component.css'],
  providers: [UsersService]
})
export class UserbanquelistComponent implements OnInit {

  namecomponent: string;

  mapUsers: StringMapEntry[] = [];

  constructor(private usersServices: UsersService) { }

  ngOnInit() {
    this.getMapAllUsers();
  }

  getMapAllUsers() {

      this.usersServices.getAllBanqueUsers(
         (usersLightBean: UserLightBean[]) => {
            for (const userLightBean of usersLightBean){
               const realName = userLightBean.lastName + ' ' + userLightBean.firstName;
               this.mapUsers.push(new StringMapEntry(userLightBean.identifierCodeUser, realName));
            }
         }
        );

  }
}
