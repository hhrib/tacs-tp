import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { User } from 'src/app/models/user';
import { UserDTO } from 'src/app/models/user.dto';
import { UsersService } from 'src/app/services/users.service';

const TEST_DATA = [{
    matchesLost: 60,
    matchesWon: 48,
    username:"test"
}]

@Component({
  selector: 'app-admin-user-stats',
  templateUrl: './admin-user-stats.component.html',
  styleUrls: ['./admin-user-stats.component.css']
})
export class AdminUserStatsComponent implements OnInit {
  displayedColumns: string[] = ['username', 'matchesLost', 'matchesWon'];
  dataSource = new MatTableDataSource();
  dataCharged = 0;
  playersList: UserDTO[] = null
  playerChoosed = new FormControl();

  constructor(
    private userService: UsersService
  ) {
    // this.playersList = [new UserDTO(5,"Pepito"), new UserDTO(6,"Fulanito")]

    this.userService.getAllUsers().subscribe(
      response => this.playersList = response,
      err => console.log(err));
   }

  ngOnInit(): void {
   
  }

  filterClick(): void {
    this.userService.getUserStatsByUsername(this.playerChoosed.value)
    .subscribe(
      (result) => {
        console.log(result)
        this.dataSource.data = [result]
        this.dataCharged = 1;
        console.log(this.dataSource.data);
      },
      err => {
        //Si el usuario todavía no perdió ni ganó
        this.dataSource.data = [{
          matchesLost: 0,
          matchesWon: 0,
          username:this.playerChoosed.value
        }]
        this.dataCharged = 1;
      }
    )
  }

}
