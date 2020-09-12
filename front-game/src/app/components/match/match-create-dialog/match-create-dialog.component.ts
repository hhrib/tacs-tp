import { Component, Inject, OnInit } from '@angular/core';
import {MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { DialogData } from '../../menu/menu.component';
import {Form, FormControl} from '@angular/forms';

@Component({
  selector: 'app-match-create-dialog',
  templateUrl: './match-create-dialog.component.html',
  styleUrls: ['./match-create-dialog.component.css']
})
export class MatchCreateDialogComponent implements OnInit {
  players = new FormControl();
  playersList : string[] = ['Juan', 'Fer', 'Ale', 'Emi', 'Hernan']

  ngOnInit(): void {
  }
  

  constructor(
    public dialogRef: MatDialogRef<MatchCreateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) 
    {
    }

  onNoClick(): void {
    this.dialogRef.close();
  }
  
}
