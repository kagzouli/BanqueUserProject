export class AccountNumberOperation {

  identifierUser: string;
  labelOperation: string;
  amount: number;

  constructor(identifierUser: string, labelOperation: string, amount: number) {
    this.identifierUser = identifierUser;
    this.labelOperation = labelOperation;
    this.amount = amount;
  }
}
