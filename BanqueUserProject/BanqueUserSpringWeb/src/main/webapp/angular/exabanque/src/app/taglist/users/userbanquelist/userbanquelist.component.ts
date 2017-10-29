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


  mapUsers: StringMapEntry[] = [ ];

  userChoose: any;

  constructor(private usersServices: UsersService) {}

  ngOnInit() {
    this.userChoose = 'ROGFED';

    this.getMapAllUsers();

  }

  getMapAllUsers() {

      this.usersServices.getAllBanqueUsers(
         (usersLightBean: UserLightBean[]) => {
           this.mapUsers = [new StringMapEntry('', 'Define your choice here')];
            for (const userLightBean of usersLightBean){
               const realName = userLightBean.lastName + ' ' + userLightBean.firstName;
               this.mapUsers.push(new StringMapEntry(userLightBean.identifierCodeUser.toString(), realName));
            }
         }
        );
    }

    updateSelectedValue(event: StringMapEntry) {
       const valueSelected = event.key;
       console.log(valueSelected);
       // this.rForm['identifierUser'] = value.key;
   }
}
