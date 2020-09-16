import { Component, Inject, OnInit } from '@angular/core';
import {MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {Form, FormControl, NgForm} from '@angular/forms';
import {MatchDTO} from '../../../models/match.dto';
import {UserDTO} from '../../../models/user.dto';
import {ProvinceDTO} from '../../../models/province.dto';

@Component({
  selector: 'app-match-create-dialog',
  templateUrl: './match-create-dialog.component.html',
  styleUrls: ['./match-create-dialog.component.css']
})
export class MatchCreateDialogComponent implements OnInit {
  //#region Creaciones que DEBEN SER RETIRADAS
  //TODO: Una vez integrado con api backend, retirar, porque viene de allá.

  player1 : UserDTO = new UserDTO(1,"Juan")
  player2 : UserDTO = new UserDTO(2,"Ale")
  player3 : UserDTO = new UserDTO(3,"Fer")
  player4 : UserDTO = new UserDTO(4,"Emi")
  player5 : UserDTO = new UserDTO(5,"Hernán")

  province1 : ProvinceDTO = new ProvinceDTO(6,"Buenos Aires")
  province2 : ProvinceDTO = new ProvinceDTO(10,"Catamarca")
  province3 : ProvinceDTO = new ProvinceDTO(22,"Chaco")
  province4 : ProvinceDTO = new ProvinceDTO(26,"Chubut")
  province5 : ProvinceDTO = new ProvinceDTO(14,"Córdoba")
  province6 : ProvinceDTO = new ProvinceDTO(18,"Corrientes")
  province7 : ProvinceDTO = new ProvinceDTO(30,"Entre Ríos")
  province8 : ProvinceDTO = new ProvinceDTO(34,"Formosa")
  province9 : ProvinceDTO = new ProvinceDTO(38,"Jujuy")
  province10 : ProvinceDTO = new ProvinceDTO(42,"La Pampa")
  province11 : ProvinceDTO = new ProvinceDTO(46,"La Rioja")
  province12 : ProvinceDTO = new ProvinceDTO(50,"Mendoza")
  province13 : ProvinceDTO = new ProvinceDTO(54,"Misiones")
  province14 : ProvinceDTO = new ProvinceDTO(58,"Neuquén")
  province15 : ProvinceDTO = new ProvinceDTO(62,"Río Negro")
  province16 : ProvinceDTO = new ProvinceDTO(66,"Salta")
  province17 : ProvinceDTO = new ProvinceDTO(70,"San Juan")
  province18 : ProvinceDTO = new ProvinceDTO(74,"San Luis")
  province19 : ProvinceDTO = new ProvinceDTO(78,"Santa Cruz")
  province20 : ProvinceDTO = new ProvinceDTO(82,"Santa fé")
  province21 : ProvinceDTO = new ProvinceDTO(86,"Santiago del Estero")
  province22 : ProvinceDTO = new ProvinceDTO(94,"Tierra del Fuego")
  province23 : ProvinceDTO = new ProvinceDTO(90,"Tucumán")
  province24 : ProvinceDTO = new ProvinceDTO(2,"CABA")

  //La inicialización más cabeza del siglo:
  playersList : UserDTO[] = [this.player1,this.player2,this.player3,this.player4,this.player5]
  provinceList: ProvinceDTO[] = [this.province1,this.province2,this.province3,this.province4,this.province5,this.province6,this.province7,this.province8,this.province9,this.province10,this.province11,this.province12,this.province13,this.province14,this.province15,this.province16,this.province17,this.province18,this.province19,this.province20,this.province21,this.province22,this.province23,this.province24]
  quantityList: number[] = [5,10,15,20]
  //#endregion


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
    this.matchInput.municipalitiesQty = form.value.quantity;
    this.matchInput.provinceId = form.value.province;
    this.matchInput.userIds = form.value.players;
    
    this.dialogRef.close(this.matchInput)
  }
  
}
