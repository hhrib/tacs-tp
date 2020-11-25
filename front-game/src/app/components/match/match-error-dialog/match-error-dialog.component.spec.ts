import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchErrorDialogComponent } from './match-error-dialog.component';

describe('MatchEndshiftDialogComponent', () => {
  let component: MatchErrorDialogComponent;
  let fixture: ComponentFixture<MatchErrorDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchErrorDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchErrorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
