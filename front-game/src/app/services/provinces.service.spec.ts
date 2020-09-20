import { TestBed } from '@angular/core/testing';

import { ProvincesService } from './provinces.service';

describe('ProvincesService', () => {
  let service: ProvincesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProvincesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
