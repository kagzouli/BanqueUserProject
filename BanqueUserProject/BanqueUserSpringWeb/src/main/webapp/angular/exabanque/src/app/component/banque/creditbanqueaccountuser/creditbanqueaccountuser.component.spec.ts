import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreditBanqueAccountUser } from './creditbanqueaccountuser.component';

describe('PocComponent', () => {
  let component: CreditBanqueAccountUser;
  let fixture: ComponentFixture<CreditBanqueAccountUser>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreditBanqueAccountUser ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreditBanqueAccountUser);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
