export class UserLightBean {

  userId: number;
  identifierCodeUser: string;
  firstName: string;
  lastName: string;

  constructor(userId?: number, identifierCodeUser?: string, firstName?: string, lastName?: string) {
    this.userId = userId;
    this.identifierCodeUser = identifierCodeUser;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
