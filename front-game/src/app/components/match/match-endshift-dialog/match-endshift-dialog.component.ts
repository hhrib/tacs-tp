import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import {MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
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
import { PassTurnDto } from 'src/app/models/passTurn.dto';
import { User } from 'src/app/models/user';
import { MessageService } from 'src/app/services/message.service';
import { AuthService } from 'src/app/services/auth.service';
import { MatchErrorDialogComponent } from '../match-error-dialog/match-error-dialog.component';
import { MatchSuccessDialogComponent } from '../match-success-dialog/match-success-dialog.component';

@Component({
  selector: 'app-match-endshift-dialog',
  templateUrl: './match-endshift-dialog.component.html',
  styleUrls: ['./match-endshift-dialog.component.css']
})
export class MatchEndshiftDialogComponent implements OnInit {

  playersList : UserDTO[] = null;
  activeUser: any = 0;


  ngOnInit(): void {
  }
  
  clicked = false;

  constructor(
    public provinceService: ProvincesService,
    public userService: UsersService,
    public dialogRef: MatDialogRef<MatchEndshiftDialogComponent>,
    public user: User,

    public match: MatchResponse,
    public matchService: MatchService,
    public route: ActivatedRoute,
    public router: Router,
    public messageService: MessageService,
    public auth: AuthService,
    public dialog: MatDialog)
    {
      this.auth.userProfile$.subscribe(
        (userProfile) => this.activeUser = userProfile.sub
      )
    }
    

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(form: NgForm){
    let passTurn = new PassTurnDto();
    passTurn.userId = this.user.id;

    let jsonBody = {
      userId : this.activeUser
    }
    
    this.matchService.passTurn(this.match.id,jsonBody).subscribe(
    result => {
      console.log("OK");
      console.log(result);
      //this.openDialogSuccessMatch();
    },
    err => {
      console.log(err);
      this.openDialogErrorMatch(err.error[0].detail);
    }
    );

    this.dialogRef.close();

    this.messageService.sendMessage("Le toca al otro player!");   

    // this.matchService.passTurnMatch(this.match.id, passTurn).subscribe(
    //   response => {
    //     console.log(response);
    //     this.router.navigate(['/mapMatch/'+this.match.id]);
    //     this.dialogRef.close();
    //   },
    //   err => {console.log(err)}
    // );

    this.messageService.sendMessage("Terminó un turno")
  }

  openDialogErrorMatch(data: string): void{
    console.log("ERROR",data);
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
