import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DebitbanqueaccountuserComponent } from './debitbanqueaccountuser.component';

describe('DebitbanqueaccountuserComponent', () => {
  let component: DebitbanqueaccountuserComponent;
  let fixture: ComponentFixture<DebitbanqueaccountuserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DebitbanqueaccountuserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DebitbanqueaccountuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
