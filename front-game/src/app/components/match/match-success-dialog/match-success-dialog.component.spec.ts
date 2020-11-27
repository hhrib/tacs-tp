import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchSuccessDialogComponent } from './match-success-dialog.component';

describe('MatchSuccessDialogComponent', () => {
  let component: MatchSuccessDialogComponent;
  let fixture: ComponentFixture<MatchSuccessDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchSuccessDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchSuccessDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
