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
import { User } from 'src/app/models/user';
import { MessageService } from 'src/app/services/message.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-match-error-dialog',
  templateUrl: './match-error-dialog.component.html',
  styleUrls: ['./match-error-dialog.component.css']
})
export class MatchErrorDialogComponent implements OnInit {

  playersList : UserDTO[] = null;
  activeUser: any = 0;

  ngOnInit(): void {
  }
  
  clicked = false;

  constructor(
    public provinceService: ProvincesService,
    public userService: UsersService,
    public dialogRef: MatDialogRef<MatchErrorDialogComponent>,
    public user: User,

    public match: MatchResponse,
    public matchService: MatchService,
    public route: ActivatedRoute,
    public router: Router,
    public messageService: MessageService,
    public auth: AuthService,
    @Inject(MAT_DIALOG_DATA) public data: any)
    {
      this.auth.userProfile$.subscribe(
        (userProfile) => this.activeUser = userProfile.sub
      )
    }
    

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(form: NgForm){
     this.dialogRef.close();
  }
}
