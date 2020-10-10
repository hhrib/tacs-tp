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
import { PassTurnDto } from 'src/app/models/passTurn.dto';

@Component({
  selector: 'app-match-endshift-dialog',
  templateUrl: './match-endshift-dialog.component.html',
  styleUrls: ['./match-endshift-dialog.component.css']
})
export class MatchEndshiftDialogComponent implements OnInit {

  playersList : UserDTO[] = null;


  ngOnInit(): void {
  }
  
  clicked = false;

  constructor(
    public provinceService: ProvincesService,
    public userService: UsersService,
    public dialogRef: MatDialogRef<MatchEndshiftDialogComponent>,
    public match: MatchResponse,
    public matchService: MatchService,
    public route: ActivatedRoute,
    public router: Router)
    {
      this.userService.getAllUsers().subscribe(
        response => this.playersList = response,
        err => console.log(err));
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(form: NgForm){
    let passTurn = new PassTurnDto();
    passTurn.userId = this.match.users[0].id;
    this.matchService.passTurnMatch(this.match.id, passTurn).subscribe(
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
