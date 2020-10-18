import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchMoveDialogComponent } from './match-move-dialog.component';

describe('MatchCreateDialogComponent', () => {
  let component: MatchMoveDialogComponent;
  let fixture: ComponentFixture<MatchMoveDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchMoveDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchMoveDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
