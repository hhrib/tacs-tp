import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchMapComponent } from './match-map.component';

describe('MatchMapComponent', () => {
  let component: MatchMapComponent;
  let fixture: ComponentFixture<MatchMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchMapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
