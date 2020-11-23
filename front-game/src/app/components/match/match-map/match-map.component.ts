import { Message } from '@angular/compiler/src/i18n/i18n_ast';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as L from 'leaflet';
import { Observable } from 'rxjs';
import { MatchDTO } from 'src/app/models/match.dto';
import { MatchResponse } from 'src/app/models/match.response';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { MapService } from 'src/app/services/map.service';
import { MarkerService } from 'src/app/services/marker.service';
import { MatchService } from 'src/app/services/matches.service';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'match-map',
  templateUrl: './match-map.component.html',
  styleUrls: ['./match-map.component.css']
})
export class MatchMapComponent implements OnInit {

  private idMatch;
  public nextTurn: string;
  public firstTurn: string;
  public defeatedPlayerNotification: string;
  public winnerPlayeNotification: string;

  constructor(
    private actRoute: ActivatedRoute, 
    private matchService: MatchService,
    private mapService: MapService, 
    private markerService: MarkerService, 
    private match: MatchResponse,
    public auth: AuthService,
    public user: User,
    private messageService: MessageService) {

      this.nextTurn = messageService.actualUserIdTurn;
      this.defeatedPlayerNotification = messageService.defeatedPlayerNotification;
      this.winnerPlayeNotification = messageService.winnerPlayerNotification;  
  }

  ngOnInit(): void {
    this.actRoute.paramMap.subscribe(params => {
      this.idMatch = params.get('id');
      this.matchService.getById(this.idMatch).subscribe(
        response => {
          console.log("Match-Map");
          this.match.id = response.id;
          this.match.date = response.date;
          this.match.config = response.config;
          this.match.map = response.map;
          this.match.state = response.state;
          this.match.users = response.users;
          this.firstTurn = response.turnPlayer.username;
          console.log(this.match);
          console.log("Fin Match-Map");
          console.log("primer turno: ", this.firstTurn)
          console.log(this.nextTurn)

          this.mapService.initMap(this.match.map?.centroide?.lat,this.match.map?.centroide?.lon);
          this.auth.userProfile$.subscribe((userProfile) => {
            this.user.id = userProfile.sub;
            this.user.username = userProfile.nickname;
            this.markerService.clearMarkers(this.mapService.map);
            this.markerService.makeMarkers(this.mapService.map);
          });
        },
        err => {console.log(err)}
      );
    });
    
  }
}