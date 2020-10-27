import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { UsersService } from 'src/app/services/users.service';

const TEST_DATA = {
  "scoreboard": [
    {"matchesLost": 1,"matchesWon": 5,"username": "Seba"},
    {"matchesLost": 3,"matchesWon": 4,"username": "Fran"},
    {"matchesLost": 4,"matchesWon": 4,"username": "Juan"},
    {"matchesLost": 5,"matchesWon": 2,"username": "Ale"},
    {"matchesLost": 6,"matchesWon": 1,"username": "Fer"},
    {"matchesLost": 9,"matchesWon": 0,"username": "Emi"},
    {"matchesLost": 9,"matchesWon": 0,"username": "Emi"},
    {"matchesLost": 9,"matchesWon": 0,"username": "Emi"},
    {"matchesLost": 9,"matchesWon": 0,"username": "Emi"},
    {"matchesLost": 9,"matchesWon": 0,"username": "Emi"},
  ]
}

@Component({
  selector: 'app-admin-scoreboard',
  templateUrl: './admin-scoreboard.component.html',
  styleUrls: ['./admin-scoreboard.component.css']
})
export class AdminScoreboardComponent implements AfterViewInit {
  displayedColumns: string[] = ['username','matchesWon','matchesLost'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator : MatPaginator;

  constructor(
    private userService: UsersService
  ) {
  //TODO: DESCOMENTAR CUANDO EL ENDPOINT SE ARREGLE.

    // this.dataSource.data = TEST_DATA.scoreboard

    this.userService.getScoreboard()
    .subscribe(
      (result) => {
        console.log(result)
        this.dataSource.data = result.scoreboard
    },
    err => console.log(err))
   }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

}
