import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormControl, NgForm } from '@angular/forms';
import { MatDatepicker, MatDatepickerInput } from '@angular/material/datepicker';
import { MatTableDataSource } from '@angular/material/table';
import {MatchService} from '../../../services/matches.service'



const TEST_DATA = [{
  cancelledMatches: 3,
  createdMatches: 19,
  finishedMatches: 8,
  inProgressMatches:36
}]

const NO_DATA_FOUND = [{
  cancelledMatches: 0,
  createdMatches: 0,
  finishedMatches: 0,
  inProgressMatches:0
}]


@Component({
  selector: 'app-admin-matches',
  templateUrl: './admin-matches.component.html',
  styleUrls: ['./admin-matches.component.css']
})
export class AdminMatchesComponent implements OnInit {
  displayedColumns: string[] = ['created', 'progress', 'finished', 'cancelled'];
  dataSource = new MatTableDataSource();
  dataCharged = 0;
  dateFrom = new FormControl(); 
  dateTo = new FormControl(); 



  constructor(
    private matchesService: MatchService,
  ) { }

  ngOnInit(): void {
  }

  filterClick(): void{
    console.log("Click filter matches!")
    let dateFromString = this.dateFrom.value.toISOString(8601).substring(0,10)
    let dateToString = this.dateTo.value.toISOString(8601).substring(0,10)
    
    this.dataCharged = 1;
    this.matchesService.getAllMatchesStatistics(dateFromString,dateToString)
    .subscribe(
      (result) => {
        this.dataSource.data = [result]      
      },
      err => this.dataSource.data = NO_DATA_FOUND
    )
  }

}
