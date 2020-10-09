import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchStateDialogComponent } from './match-state-dialog.component';

describe('MatchStateDialogComponent', () => {
  let component: MatchStateDialogComponent;
  let fixture: ComponentFixture<MatchStateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchStateDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchStateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
