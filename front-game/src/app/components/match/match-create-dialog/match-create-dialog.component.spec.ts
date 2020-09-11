import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchCreateDialogComponent } from './match-create-dialog.component';

describe('MatchCreateDialogComponent', () => {
  let component: MatchCreateDialogComponent;
  let fixture: ComponentFixture<MatchCreateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchCreateDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
