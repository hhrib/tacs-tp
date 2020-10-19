import { TestBed } from '@angular/core/testing';

import { MunicipalitiesService } from './municipalities.service';

describe('MunicipalitiesService', () => {
  let service: MunicipalitiesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MunicipalitiesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
