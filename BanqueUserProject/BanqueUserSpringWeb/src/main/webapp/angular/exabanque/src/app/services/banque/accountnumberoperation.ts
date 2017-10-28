export class AccountNumberOperation {

  identifier: string;
  labelOperation: string;
  amount: number;

  constructor(identifier: string, labelOperation: string, amount: number) {
    this.identifier = identifier;
    this.labelOperation = labelOperation;
    this.amount = amount;
  }
}
