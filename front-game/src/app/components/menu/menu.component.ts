import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';

import { MatchService } from '../../services/matches.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatSelectModule} from '@angular/material/select';
import { AuthService } from '../../services/auth.service';


import { MatchCreateDialogComponent } from '../../components/match/match-create-dialog/match-create-dialog.component';
import { MatchSearchComponent } from '../match/match-search/match-search.component'
import { ProvincesService } from 'src/app/services/provinces.service';
import { windowWhen } from 'rxjs/operators';
import { MatchDTO } from 'src/app/models/match.dto';
import { MatchResponse } from 'src/app/models/match.response';
import { MatchAtackDialogComponent } from '../match/match-atack-dialog/match-atack-dialog.component';
import { MatchMoveDialogComponent } from '../match/match-move-dialog/match-move-dialog.component';
import { MatchStateDialogComponent } from '../match/match-state-dialog/match-state-dialog.component';
import { MatchEndshiftDialogComponent } from '../match/match-endshift-dialog/match-endshift-dialog.component';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  providers:[
    MatchService,
  ]
})
export class MenuComponent implements OnInit {

  constructor(
    public matchService: MatchService,
    public dialog: MatDialog,
    public auth: AuthService,
    public user: User,
    public match: MatchDTO,
    public matchOutput: MatchResponse,
    public route: ActivatedRoute,
    public router: Router,
    ) { }

  ngOnInit(): void {
    this.auth.userProfile$.subscribe(result=> {
      console.log("Menu - userProfile", result);
      this.user.id = result?.sub;
      this.user.username = result?.nickname;
    });
  }

  openDialogCreateMatch(): void{
    //Crear promise para recibir parámetros del front, y luego:

    //this.matchService.createMatch(match);
    const dialogRef = this.dialog.open(MatchCreateDialogComponent, {
      height: '450px',
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
    });
  }

  openMap(): void{
    this.router.navigate(['/mapMatch/'+this.matchOutput.id]);
  }

  openDialogAtackMatch(): void{
    const dialogRef = this.dialog.open(MatchAtackDialogComponent, {
      height: '350px',
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openDialogMoveMatch(): void{
    const dialogRef = this.dialog.open(MatchMoveDialogComponent, {
      height: '350px',
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openDialogStateMatch(): void{
    const dialogRef = this.dialog.open(MatchStateDialogComponent, {
      height: '300px',
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openDialogEndshiftMatch(): void{
    const dialogRef = this.dialog.open(MatchEndshiftDialogComponent, {
      height: '150px',
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
