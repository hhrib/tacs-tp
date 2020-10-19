import { AfterViewInit, Component, OnInit, Input,ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule} from '@angular/material/table'
import { FindMatchDTO } from 'src/app/models/findMatch.dto';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { WebSocketService } from 'src/app/services/web-socket.service';
import { MatchService } from 'src/app/services/matches.service';
@Component({
  selector: 'statistics-panel',
  templateUrl: './statistics-panel.component.html',
  styleUrls: ['./statistics-panel.component.css']
})
export class StatisticsPanelComponent implements OnInit {
  response;
  input;
  @Input() municipalities: Array<Object>;
  constructor(public webSocketService: WebSocketService,public matchService : MatchService) {}
  
  
  sendMessage() {
    if (this.input) {
      this.webSocketService.sendMessage(this.input);
      this.input = '';
    }
  }

  ngOnInit() {
    //this.matchService.getMatchMuniStatistics("1",2).subscribe(data=>{
      //console.log(data);
      //this.response = data;
    //})  
  }

}
