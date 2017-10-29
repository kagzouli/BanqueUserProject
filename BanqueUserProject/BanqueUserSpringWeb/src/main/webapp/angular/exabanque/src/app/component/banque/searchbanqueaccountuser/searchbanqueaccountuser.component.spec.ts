import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchbanqueaccountuserComponent } from './searchbanqueaccountuser.component';

describe('SearchbanqueaccountuserComponent', () => {
  let component: SearchbanqueaccountuserComponent;
  let fixture: ComponentFixture<SearchbanqueaccountuserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchbanqueaccountuserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchbanqueaccountuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
