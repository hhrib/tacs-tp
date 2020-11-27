import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import {MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
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
import { Municipality } from 'src/app/models/municipality';
import { Atack } from 'src/app/models/atack.dto';
import { User } from 'src/app/models/user';
import { MarkerService } from 'src/app/services/marker.service';
import { MapService } from 'src/app/services/map.service';
import { MatchErrorDialogComponent } from '../match-error-dialog/match-error-dialog.component';
import { MatchSuccessDialogComponent } from '../match-success-dialog/match-success-dialog.component';

@Component({
  selector: 'app-match-atack-dialog',
  templateUrl: './match-atack-dialog.component.html',
  styleUrls: ['./match-atack-dialog.component.css']
})
export class MatchAtackDialogComponent implements OnInit {

  //#region Configuración de números de lógica. Posiblemente luego se traigan del back.
  from = new ModesDTO("Fortress", [12,8,1,1,1.25]);
  to = new ModesDTO("Rebelion", [15,10,2,2,1.25]);
  war = new ModesDTO("War", [16,9,2,2,1.10]);
  //#endregion


  playersList : UserDTO[] = null;
  municipalityList: Municipality[] = null;
  municipalityEnemyList: Municipality[] = null;

  ngOnInit(): void {
  }
  
  clicked = false;

  constructor(
    public provinceService: ProvincesService,
    public userService: UsersService,
    public dialogRef: MatDialogRef<MatchAtackDialogComponent>,
    public user: User,
    public match: MatchResponse,
    public matchService: MatchService,
    public mapService: MapService,
    public markerService: MarkerService,
    public route: ActivatedRoute,
    public router: Router,
    public dialog: MatDialog)
    {
      console.log("Atack", this.user);
      this.municipalityList = match.map.municipalities
        .filter(x => x.owner.id == this.user.id); 
      this.municipalityEnemyList = match.map.municipalities
      .filter(x => x.owner.id != this.user.id);

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
    let atack = new Atack();
    atack.gauchosQty = form.value.gauchosQty;
    atack.muniAttackingId = form.value.municipalityAtacking;
    atack.muniDefendingId = form.value.municipalityDefending;
    this.matchService.atackMatchMunicipalities(this.match.id,atack).subscribe(
      response => {
        console.log(response);
        
        this.matchService.getById(this.match.id).subscribe(
          response => {
            console.log("SearchMatch in atack");
            this.match.id = response.id;
            this.match.date = response.date;
            this.match.config = response.config;
            this.match.map = response.map;
            this.match.state = response.state;
            this.match.users = response.users;
            console.log(this.match);

            this.markerService.clearMarkers(this.mapService.map);
            this.markerService.makeMarkers(this.mapService.map);
    
            this.router.navigate(['/mapMatch/'+this.match.id]);
            this.dialogRef.close();

            console.log("Fin SearchMatch in atack");
            this.openDialogSuccessMatch();
          },
          err => {
            console.log(err);
            this.openDialogErrorMatch(err.error[0].detail);
          });
      },
      err => {
        console.log(err);
        this.openDialogErrorMatch(err.error[0].detail);
      }
    );
    this.dialogRef.close();
  }

  openDialogErrorMatch(data: any): void{
    const dialogRef = this.dialog.open(MatchErrorDialogComponent, {
      width: '300px',
      data: {
        message: data,
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openDialogSuccessMatch(): void{
    console.log("OK");
    const dialogRef = this.dialog.open(MatchSuccessDialogComponent, {
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
