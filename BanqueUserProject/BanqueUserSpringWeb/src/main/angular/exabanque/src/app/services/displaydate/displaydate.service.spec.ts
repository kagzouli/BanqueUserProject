import { TestBed, inject } from '@angular/core/testing';

import { DisplaydateService } from './displaydate.service';

describe('DisplaydateService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DisplaydateService]
    });
  });

  it('should be created', inject([DisplaydateService], (service: DisplaydateService) => {
    expect(service).toBeTruthy();
  }));
});
