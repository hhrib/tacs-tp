import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as L from 'leaflet';
import { MatchDTO } from 'src/app/models/match.dto';
import { MatchResponse } from 'src/app/models/match.response';
import { MarkerService } from 'src/app/services/marker.service';
import { MatchService } from 'src/app/services/matches.service';

const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});
L.Marker.prototype.options.icon = iconDefault;

@Component({
  selector: 'match-map',
  templateUrl: './match-map.component.html',
  styleUrls: ['./match-map.component.css']
})
export class MatchMapComponent implements OnInit {

  private map;
  private idMatch;

  constructor(
    private actRoute: ActivatedRoute, 
    private matchService: MatchService, 
    private markerService: MarkerService, 
    private match: MatchResponse) {
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
          console.log(this.match);
          console.log("Fin Match-Map");
          this.initMap(this.match.map?.centroide?.lat,this.match.map?.centroide?.lon);
          this.markerService.makeMunicipalitiesMarkers(this.map);
        },
        err => {console.log(err)}
      );
    });
    
  }

  private initMap(lat: string, lon: string): void {
    
    if(lat == null || lon == null)
    {
      lat = "-37";
      lon = "-60.5";
    }

    this.map = L.map('map', {
      center: [Number(lat), Number(lon)],
      zoom: 6,
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      minZoom: 5,
      maxZoom: 7,
      attribution: '© <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
  }
}