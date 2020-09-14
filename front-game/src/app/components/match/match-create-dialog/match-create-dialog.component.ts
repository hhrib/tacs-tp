import { Component, Inject, OnInit } from '@angular/core';
import {MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {Form, FormControl, NgForm} from '@angular/forms';
import {MatchDTO} from '../../../models/match.dto';

@Component({
  selector: 'app-match-create-dialog',
  templateUrl: './match-create-dialog.component.html',
  styleUrls: ['./match-create-dialog.component.css']
})
export class MatchCreateDialogComponent implements OnInit {
  playersList : string[] = ['Juan', 'Fer', 'Ale', 'Emi', 'Hernan']
  provinceList: string[] = ["Buenos aires", "Catamarca", "Chaco", "Chubut", "Córdoba", "Corrientes", "Entre Ríos", "Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones", "Neuquén", "Río Negro", "Salta", "San Juan", "San Luis", "Santa Cruz", "Santa Fe", "Santiago del Estero", "Tierra del Fuego", "Tucumán"]
  quantityList: number[] = [5,10,15,20]

  ngOnInit(): void {
  }
  
  constructor(
    public dialogRef: MatDialogRef<MatchCreateDialogComponent>,
    public matchInput: MatchDTO)
    {
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(form: NgForm){
    this.matchInput.prov = form.value.province;
    this.matchInput.muni_quantity = form.value.quantity;
    this.matchInput.players = form.value.players;
    
    this.dialogRef.close(this.matchInput)
  }
  
}
