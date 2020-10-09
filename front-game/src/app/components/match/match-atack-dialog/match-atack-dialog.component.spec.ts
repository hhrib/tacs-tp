import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchAtackDialogComponent } from './match-atack-dialog.component';

describe('MatchEndshiftDialogComponent', () => {
  let component: MatchAtackDialogComponent;
  let fixture: ComponentFixture<MatchAtackDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchAtackDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchAtackDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
