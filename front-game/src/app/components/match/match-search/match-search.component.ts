import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule} from '@angular/material/table'
import { FindMatchDTO } from 'src/app/models/findMatch.dto';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';

const ELEMENT_DATA: FindMatchDTO[] = [
  {idMatch: 1, province:"Buenos Aires", state: "In progress", usersPlaying: ['Juan','Fer','Emi'], creationDate: "2020/09/17"},
  {idMatch: 2, province:"Tierra del Fuego", state: "Finished", usersPlaying: ['Ale','Fer','Emi'], creationDate: "2020/09/16"},
  {idMatch: 3, province:"Santa Cruz", state: "Finished", usersPlaying: ['Juan','Ale','Hernan'], creationDate: "2020/09/17"},
  {idMatch: 4, province:"Córdoba", state: "In progress", usersPlaying: ['Juan','Fer','Emi'], creationDate: "2020/09/18"},
  {idMatch: 5, province:"San Luis", state: "Finished", usersPlaying: ['Hernán','Fer','Emi'], creationDate: "2020/09/19"},
  {idMatch: 6, province:"Misiones", state: "In progress", usersPlaying: ['Juan','Fer','Emi'], creationDate: "2020/09/19"},
  {idMatch: 7, province:"Corrientes", state: "Finished", usersPlaying: ['Juan','Hernán','Emi'], creationDate: "2020/09/19"},
  {idMatch: 8, province:"Misiones", state: "In progress", usersPlaying: ['Ale','Fer','Hernán'], creationDate: "2020/09/19"},
  {idMatch: 9, province:"Buenos Aires", state: "In progress", usersPlaying: ['Ale','Fer','Emi'], creationDate: "2020/09/19"},
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
  displayedColumns: string[] = ['idMatch', 'province', 'state', 'players', 'date'];
  
  dataSource = new MatTableDataSource(ELEMENT_DATA);
  @ViewChild(MatSort) sort : MatSort;
  @ViewChild(MatPaginator) paginator : MatPaginator;
  searchKey : string;

/*   constructor() {
    setTimeout(() => {
      this.dataSource = ELEMENT_DATA;
    }, 2000);
   }
 */
  ngAfterViewInit() {
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


