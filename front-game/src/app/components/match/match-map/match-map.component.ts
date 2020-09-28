import { Component, OnInit } from '@angular/core';
import * as L from 'leaflet';
import { MatchDTO } from 'src/app/models/match.dto';
import { MatchResponse } from 'src/app/models/Response/match.response';
import { MarkerService } from 'src/app/services/marker.service';

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

  constructor(private match: MatchResponse, private markerService: MarkerService) {
  }

  ngOnInit(): void {
    console.log("MatchResponse: ");
    console.log(this.match);
    //this.initMap(this.match.map.centroide.lat, this.match.map.centroide.lon);
    this.initMap();
    this.markerService.makeMunicipalitiesMarkers(this.map);
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [-37, -60.5],
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