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
    this.dataSource.data = []
  }

  filterClick(): void{
    console.log("Click filter matches!")
    console.log(this.dateFrom.value)
    let dateFromString = this.dateFrom.value.toLocaleDateString().replaceAll("/","-")
    let dateToString = this.dateTo.value.toLocaleDateString().replaceAll("/","-")
    console.log(dateFromString);
    console.log(dateToString);
    
    this.dataCharged = 1;
    // this.matchesService.getAllMatchesStatistics(dateFromString,dateToString)
    // .subscribe(
    //   (result) => {
    //     this.dataSource.data = result
    //   },
    //   err => console.log(err)
    // )

    this.dataSource.data = TEST_DATA
  }

}
