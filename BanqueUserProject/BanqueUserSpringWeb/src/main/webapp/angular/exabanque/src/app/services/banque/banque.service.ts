import { Injectable } from '@angular/core';

@Injectable()
export class BanqueService {

  constructor() { }

  creditAccountNumber(identifierUser : string, labelOperation : string, amount: number){
    console.log("Credit account number service");
    
    console.log("Service IdentifierUser : " + identifierUser);
    console.log("Service labelOperation : " + labelOperation);
    console.log("Service amount : " + amount);
  }

}
