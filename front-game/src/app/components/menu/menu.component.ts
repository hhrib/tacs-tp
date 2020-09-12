import { Component, OnInit } from '@angular/core';
import { MatchService } from '../../services/matches.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatSelectModule} from '@angular/material/select';


import { MatchCreateDialogComponent } from '../../components/match/match-create-dialog/match-create-dialog.component';

export interface DialogData {
  animal: string;
  name: string;
}

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  providers:[
    MatchService
  ]
})
export class MenuComponent implements OnInit {

  animal: string;
  name: string;

  constructor(
    public matchService: MatchService,
    public dialog: MatDialog,
    ) { }

  ngOnInit(): void {
  }

  openDialogCreateMatch(): void{
    //Crear promise para recibir parámetros del front, y luego:

    //this.matchService.createMatch(match);
    const dialogRef = this.dialog.open(MatchCreateDialogComponent, {
      height: '400px',
      width: '300px',
      data: {name: this.name, animal: this.animal}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.animal = result;
    });
  }

  openDialogSearchMatches(){

  }
  /*
    openCreateModal() {
      const modalRef = this.modalService.open(DeviceModalComponent, { size: 'lg' });
      const modal: DeviceModalComponent = modalRef.componentInstance;
      modal.disabled = false;
      modalRef.result
          .then(device => {
              if (device) {
                  const createSubscription = this.deviceService.createDevice(device).subscribe(
                      response => {
                          createSubscription.unsubscribe();
                          this.deviceService.getDevices();
                      }
                  );
              }
          })
          .catch(_=>{});
  }
  */
}
