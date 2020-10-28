import { Component, OnInit } from '@angular/core';

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  tiles: Tile[] = [
    {text: 'Matches', cols: 2, rows: 1, color: '#6bd1f3'},
    {text: 'Scoreboard', cols: 1, rows: 2, color: 'rgb(86 167 214)'},
    {text: 'UserStats', cols: 2, rows: 1, color: '#6bc1f7'},
  ];

  constructor() { }

  ngOnInit(): void {
  }

}
