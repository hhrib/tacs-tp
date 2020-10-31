import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import {MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {Form, FormControl, NgForm} from '@angular/forms';
import {ModesDTO} from '../../../models/modes.dto';
import {UserDTO} from '../../../models/user.dto';
import { ProvincesService } from 'src/app/services/provinces.service';
import { UsersService } from 'src/app/services/users.service';
import { MatchService } from 'src/app/services/matches.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatchResponse } from 'src/app/models/match.response';
import { Municipality } from 'src/app/models/municipality';
import { Move } from 'src/app/models/move.dto';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-match-move-dialog',
  templateUrl: './match-move-dialog.component.html',
  styleUrls: ['./match-move-dialog.component.css']
})
export class MatchMoveDialogComponent implements OnInit {

  playersList : UserDTO[] = null;
  municipalityList: Municipality[] = null;

  ngOnInit(): void {
  }
  
  clicked = false;

  constructor(
    public provinceService: ProvincesService,
    public userService: UsersService,
    public dialogRef: MatDialogRef<MatchMoveDialogComponent>,
    public user: User,
    public match: MatchResponse,
    public matchService: MatchService,
    public route: ActivatedRoute,
    public router: Router)
    {
      this.municipalityList = match.map.municipalities
        .filter(x => x.owner.id == this.user.id); 

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
    let move = new Move();
    move.idDestinyMuni = form.value.municipalityDest;
    move.idOriginMuni = form.value.municipalityOri;
    move.qty = form.value.gauchosQty;
    this.matchService.moveMatchMunicipalities(this.match.id,move).subscribe(
      response => {
        console.log(response);
        this.router.navigate(['/mapMatch/'+this.match.id]);
        this.dialogRef.close();
      },
      err => {console.log(err)}
    );
    this.dialogRef.close();
  }
}
