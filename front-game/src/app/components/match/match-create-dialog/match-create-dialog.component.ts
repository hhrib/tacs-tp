import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import {MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {Form, FormControl, NgForm} from '@angular/forms';
import {ModesDTO} from '../../../models/modes.dto';
import {MatchDTO} from '../../../models/match.dto';
import {UserDTO} from '../../../models/user.dto';
import {ProvinceDTO} from '../../../models/province.dto';
import { ProvincesService } from 'src/app/services/provinces.service';
import { UsersService } from 'src/app/services/users.service';
import { MatchService } from 'src/app/services/matches.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatchResponse } from 'src/app/models/match.response';

@Component({
  selector: 'app-match-create-dialog',
  templateUrl: './match-create-dialog.component.html',
  styleUrls: ['./match-create-dialog.component.css']
})
export class MatchCreateDialogComponent implements OnInit {

  //#region Configuración de números de lógica. Posiblemente luego se traigan del back.
  fortress = new ModesDTO("Fortress", [12,8,1,1,1.25]);
  rebelion = new ModesDTO("Rebelion", [15,10,2,2,1.25]);
  war = new ModesDTO("War", [16,9,2,2,1.10]);
  //#endregion


  playersList : UserDTO[] = null;
  provinceList: ProvinceDTO[] = null;
  quantityList: number[] = [10,15,20];
  modeList: ModesDTO[] = [this.fortress,this.rebelion,this.war];
  gauchosQtyList: number[] = [10,20,30,40,50];

  ngOnInit(): void {
  }
  
  clicked = false;

  constructor(
    public provinceService: ProvincesService,
    public userService: UsersService,
    public dialogRef: MatDialogRef<MatchCreateDialogComponent>,
    public matchInput: MatchDTO,
    public match: MatchResponse,
    public matchService: MatchService,
    public route: ActivatedRoute,
    public router: Router)
    {
      this.provinceService.getProvincesForCreation().subscribe(
        response => this.provinceList = response,
        err => console.log(err));

      this.userService.getAllUsers().subscribe(
        response => this.playersList = response,
        err => console.log(err));
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(form: NgForm){
    this.clicked = true;
    this.dialogRef.disableClose = true;
    this.matchInput.municipalitiesQty = form.value.quantity;
    this.matchInput.provinceId = form.value.province;
    this.matchInput.userIds = form.value.players;
    this.matchInput.configs = form.value.mode;
    this.matchInput.configs.push(form.value.gauchosQty);
    //this.matchInput.configs = allConf;
    this.matchService.createMatch(this.matchInput).subscribe(
      response => {
        console.log("CreateMatch");
        this.match.id = response.id;
        this.match.date = response.date;
        this.match.config = response.config;
        this.match.map = response.map;
        this.match.state = response.state;
        this.match.users = response.users;
        console.log(this.match);
        console.log("Fin CreateMatch");
        this.matchService.newTurnMatch(this.match.id).subscribe(
          response => {
            console.log(response);
            // this.router.navigate(['/mapMatch/'+this.match.id]);
            this.dialogRef.close(this.matchInput);
          },
          err => {
            console.log(err);
            this.clicked = false;
          });
      },
      err => {
        console.log(err);
        this.clicked = false;
      });
  }
}
