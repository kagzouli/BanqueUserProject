export class SearchAccountOperation {

  userIdentifier: string;
  beginDate: Date;
  endDate: Date;
  operationType: string;
  operationDate: Date;

  constructor(userIdentifier?: string, beginDate?: Date, endDate?: Date) {
    this.userIdentifier = userIdentifier;
    this.beginDate = beginDate;
    this.endDate = endDate;
  }
}
