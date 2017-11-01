import { Component, OnInit , Input } from '@angular/core';

import { UsersService } from '../../../services/users/users.service';

import {UserLightBean} from '../../../services/users/userlightbean';

import {StringMapEntry} from '../../../services/stringmapentry';
import { FormGroup } from '@angular/forms';



@Component({
  selector: 'app-userbanquelist',
  templateUrl: './userbanquelist.component.html',
  styleUrls: ['./userbanquelist.component.css'],
  providers: [UsersService]
})
export class UserbanquelistComponent implements OnInit {


  mapUsers: StringMapEntry[] = [ ];

  @Input('value') userChoose: string = '';

  @Input() currentForm: FormGroup;

  constructor(private usersServices: UsersService) {}

  ngOnInit() {
    this.getMapAllUsers();
    
    let identifierUser = this.currentForm['identifierUser'];
    if (identifierUser != null){
        this.userChoose = identifierUser;
    }
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

    updateSelectedValue(event: string) {
       const valueSelected = event;
       // this.rForm['identifierUser'] = value.key;
   }
}
