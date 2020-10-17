import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';

import { MatchService } from '../../services/matches.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatSelectModule} from '@angular/material/select';
import { AuthService } from '../../services/auth.service';


import { MatchCreateDialogComponent } from '../../components/match/match-create-dialog/match-create-dialog.component';
import { MatchSearchComponent } from '../match/match-search/match-search.component'
import { ProvincesService } from 'src/app/services/provinces.service';
import { concatMap, pluck, tap } from 'rxjs/operators';
import { Auth0Client } from '@auth0/auth0-spa-js';
import { MessageService } from 'src/app/services/message.service';
import { stringify } from 'querystring';
import { Observable, of } from 'rxjs';
import { OnReadOpts } from 'net';
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

//TODO: Limpiar OnDestroy -> websocket debe ir en otro componente
export class MenuComponent implements OnInit, OnDestroy {
  alreadyInMatch: number = 0;
  activeUser: string = null
  
  constructor(
    public matchService: MatchService,
    public dialog: MatDialog,
    public auth: AuthService,
    public messageService: MessageService, //TODO: Retirar, se usó para probar el passTurn que debe ir en otro componente.
    public user: User,
    public match: MatchDTO,
    public matchOutput: MatchResponse,
    public route: ActivatedRoute,
    public router: Router,
    ){}

  ngOnInit(): void {
    //TODO: Corregir sincronismo con promesas entre nuestra api y auth0.
    setTimeout(() => {
      this.auth.isAuthenticated$.subscribe((isAuth) => {
        if(isAuth){
          this.auth.userProfile$.subscribe((userProfile) => {
            this.user.id = userProfile.sub;
            this.user.username = userProfile.nickname;
              this.activeUser = userProfile.sub;
              this.matchService.getUserAlreadyInMatch(userProfile.sub)
              .subscribe(
                (matchId) => this.alreadyInMatch = matchId.matchId,
                err => this.alreadyInMatch = 0
            )
          })
        };
      })
      
    }, 2000);
  }

  ngOnDestroy(): void {
    this.messageService.disconnect();
  }

  openDialogCreateMatch(): void {
    const dialogRef = this.dialog.open(MatchCreateDialogComponent, {
      height: '450px',
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      setTimeout(() => {
        this.matchService.getUserAlreadyInMatch(this.activeUser).subscribe(
          (res) => {
            if (res){
              this.alreadyInMatch = res.matchId
              console.log("El usuario actual se unió a la partida número: " + this.alreadyInMatch)
            }
          }
        )
      }, 2000); 
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

  //TODO: Trasladar método a interfaz donde se interactúa con el mapa
  passTurn(): void {
    let jsonBody = {
      userId : this.activeUser
    }
     this.matchService.passTurn(this.alreadyInMatch,jsonBody).subscribe()

    this.messageService.sendMessage("Le toca al otro player!")

  }

  joinGameByWebSocket(): void {
    this.router.navigate(['/mapMatch/'+this.alreadyInMatch]);
    this.messageService.connect(this.alreadyInMatch, this.activeUser);
  }

}
