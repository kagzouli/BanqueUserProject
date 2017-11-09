export class ExaAccountOperation {

  userIdentifier: string;
  label: string;
  amount: number;
  operationType: string;
  operationDate: Date;

  constructor(userIdentifier?: string, label?: string, amount?: number, operationType?: string, operationDate?: Date) {
    this.userIdentifier = userIdentifier;
    this.label = label;
    this.amount = amount;
    this.operationType = operationType;
    this.operationDate = operationDate;
  }
}
