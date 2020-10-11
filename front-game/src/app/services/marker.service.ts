import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

import * as L from 'leaflet';
import { PopUpService } from './pop-up.service';
import { MatchResponse } from '../models/match.response';
import { User } from '../models/user';

const MATCH_URL = environment.BASE_URL + 'matches';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const redIcon = L.icon({
  iconUrl: "assets/redIcon.png",
  shadowUrl: "assets/marker-shadow.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});

@Injectable()
export class MarkerService {

  constructor(
    private http: HttpClient, 
    private popupService: PopUpService, 
    private match: MatchResponse,
    private user: User) {
  }

  makeMunicipalitiesMarkers(map: L.Map): void {

    var self = this;
    this.match.map.municipalities.forEach(function (value) {
      let lat = value.centroide.lon;
      let lon = value.centroide.lat;
      let marker: L.Marker<any>; 

      if(value.owner.id == self.user.id)
        marker = L.marker([Number(lon), Number(lat)]).addTo(map);
      else
        marker = L.marker([Number(lon), Number(lat)], {icon: redIcon}).addTo(map);


      marker.bindPopup(self.popupService.makeMunicipalitesPopup(value));
    }); 
    
    // this.getMunicipalities().subscribe((res: any) => {
    //   for (const c of res.features) {
    //     const lat = c.geometry.coordinates[0];
    //     const lon = c.geometry.coordinates[1];
    //     const marker = L.marker([lon, lat]).addTo(map);
    //   }
    // });
  }

  // public getMunicipalities(): Observable<any> {
  //   return this.http.get<any>(`${MATCH_URL}`)
  // }
}