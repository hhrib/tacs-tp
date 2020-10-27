import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminScoreboardComponent } from './admin-scoreboard.component';

describe('AdminScoreboardComponent', () => {
  let component: AdminScoreboardComponent;
  let fixture: ComponentFixture<AdminScoreboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminScoreboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminScoreboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
