import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatchDTO } from 'src/app/models/match.dto';
import { MatchService } from 'src/app/services/matches.service';

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.css']
})
export class MatchComponent implements OnInit {

  constructor(private route: ActivatedRoute, public matchService : MatchService, public match : MatchDTO) { }

  ngOnInit(): void {
    console.log("MatchDto:")
    console.log(JSON.stringify(this.match))
  }

  getMatch(): void {
    
    this.matchService.getById(this.match)
      .subscribe(match => this.match = match);
  }
}
