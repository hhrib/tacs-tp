import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule} from '@angular/material/table'
import { FindMatchDTO } from 'src/app/models/findMatch.dto';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatchService } from 'src/app/services/matches.service';

const ELEMENT_DATA: FindMatchDTO[] = [
  {id: null, map:"Buenos Aires", state: "In progress", users: ['Juan','Fer','Emi'], date: "2020/09/17"},
  {id: null, map:"Tierra del Fuego", state: "Finished", users: ['Ale','Fer','Emi'], date: "2020/09/16"},
  {id: null, map:"Santa Cruz", state: "Finished", users: ['Juan','Ale','Hernan'], date: "2020/09/17"},
  {id: null, map:"Córdoba", state: "In progress", users: ['Juan','Fer','Emi'], date: "2020/09/18"},
  {id:null, map:"San Luis", state: "Finished", users: ['Hernán','Fer','Emi'], date: "2020/09/19"},
  {id: null, map:"Misiones", state: "In progress", users: ['Juan','Fer','Emi'], date: "2020/09/19"},
  {id: null, map:"Corrientes", state: "Finished", users: ['Juan','Hernán','Emi'], date: "2020/09/19"},
  {id: null, map:"Misiones", state: "In progress", users: ['Ale','Fer','Hernán'], date: "2020/09/19"},
  {id: null, map:"Buenos Aires", state: "In progress", users: ['Ale','Fer','Emi'], date: "2020/09/19"},
]

/*
public idMatch : number;
public province : string;
public muniQty : number;
public usersPlaying: string[]
public creationDate: Date
*/
@Component({
  selector: 'app-match-search',
  templateUrl: './match-search.component.html',
  styleUrls: ['./match-search.component.css']
})
export class MatchSearchComponent implements AfterViewInit {
  displayedColumns: string[] = ['id', 'map', 'state', 'users', 'date'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort) sort : MatSort;
  @ViewChild(MatPaginator) paginator : MatPaginator;
  searchKey : string;

  constructor(public matchService : MatchService) {}

  ngAfterViewInit() {
      this.matchService.getMatches().subscribe(
      result => this.dataSource.data = result,
      err => console.log(err));

    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  onSearchClear() {
    this.searchKey = "";
    this.applyFilter();
  }

  applyFilter(){
    this.dataSource.filter = this.searchKey.trim().toLocaleLowerCase();
  }

}


