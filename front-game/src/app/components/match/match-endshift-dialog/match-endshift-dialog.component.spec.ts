import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchEndshiftDialogComponent } from './match-endshift-dialog.component';

describe('MatchEndshiftDialogComponent', () => {
  let component: MatchEndshiftDialogComponent;
  let fixture: ComponentFixture<MatchEndshiftDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchEndshiftDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchEndshiftDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
