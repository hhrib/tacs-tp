import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule} from '@angular/material/table'
import { FindMatchDTO } from 'src/app/models/findMatch.dto';
import { MuniStatisticsDTO } from 'src/app/models/muniStatistics.dto';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatchService } from 'src/app/services/matches.service';
import {StatisticsPanelComponent} from '../statistics-panel/statistics-panel.component';
import { HttpClient } from '@angular/common/http';



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
  displayedColumns: string[] = ['id', 'map', 'state', 'users', 'date', 'view'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort) sort : MatSort;
  @ViewChild(MatPaginator) paginator : MatPaginator;
  searchKey : string;
  municipalities:MuniStatisticsDTO[];
  selectedMuni:object;
  selectedMatch;
  pagePhoto:any;
  constructor(public matchService : MatchService, private http: HttpClient) {}

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

  viewDetail(match) {
    this.selectedMatch=match;
    this.matchService.getMatchMunicipalities(match.id).
    subscribe(
      municipalities => {
        this.municipalities = municipalities;
      err => console.log(err)});
  }
  
  
  viewDetailMuni(muni) {
    this.matchService.getMatchMuniStatistics(this.selectedMatch.id,muni.muniId).
    subscribe(
      result => {
        this.selectedMuni = result;
        debugger;
        alert(result);},
      err => console.log(err));
      //this.http.get("https://pixabay.com/api/?key=18484881-06f0c36cb201968b0204f815a&q="+this.selectedMatch.map+"+"+muni.muniName+"&image_type=photo&page=1&per_page=3")
      this.http.get("https://pixabay.com/api/?key=18484881-06f0c36cb201968b0204f815a&q=yellow+flowers&image_type=photo&page=1&per_page=3")
      .subscribe(data=>{
               this.pagePhoto=data;
      });  
  }
  

}


