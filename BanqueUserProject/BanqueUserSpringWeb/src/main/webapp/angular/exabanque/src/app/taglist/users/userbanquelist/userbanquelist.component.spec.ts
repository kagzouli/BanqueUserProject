import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserbanquelistComponent } from './userbanquelist.component';

describe('UserbanquelistComponent', () => {
  let component: UserbanquelistComponent;
  let fixture: ComponentFixture<UserbanquelistComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserbanquelistComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserbanquelistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
