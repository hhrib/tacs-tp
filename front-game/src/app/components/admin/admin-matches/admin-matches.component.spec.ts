import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminMatchesComponent } from './admin-matches.component';

describe('AdminMatchesComponent', () => {
  let component: AdminMatchesComponent;
  let fixture: ComponentFixture<AdminMatchesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminMatchesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminMatchesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
