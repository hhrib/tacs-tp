import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

import * as L from 'leaflet';
import { PopUpService } from './pop-up.service';
import { MatchResponse } from '../models/match.response';
import { User } from '../models/user';
import { MunicipalitiesService } from './municipalities.service';

const MATCH_URL = environment.BASE_URL + 'matches';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const shadowUrl = 'assets/marker-shadow.png';

const redIcon = L.icon({
  shadowUrl,
  iconRetinaUrl: 'assets/marker-icon-2x-red.png',
  iconUrl: 'assets/marker-icon-red.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});

const goldIcon = L.icon({
  shadowUrl,
  iconRetinaUrl: 'assets/marker-icon-2x-gold.png',
  iconUrl: 'assets/marker-icon-gold.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});

const greenIcon = L.icon({
  shadowUrl,
  iconRetinaUrl: 'assets/marker-icon-2x-green.png',
  iconUrl: 'assets/marker-icon-green.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});

const blackIcon = L.icon({
  shadowUrl,
  iconRetinaUrl: 'assets/marker-icon-2x-black.png',
  iconUrl: 'assets/marker-icon-black.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});

const arrIcons = new Array(redIcon, goldIcon, greenIcon, blackIcon);

@Injectable()
export class MarkerService {
  private markers = new Array();
  constructor(
    private popupService: PopUpService, 
    private match: MatchResponse,
    private municipalities: MunicipalitiesService,
    private user: User) {
  }

  clearMarkers(map: L.Map): void {
    console.log("Clear Markers");
    this.markers?.forEach(marker => {
      map.removeLayer(marker)
    });
  }

  makeMarkers(map: L.Map): void {
    console.log("Make Markers");
    var arrIconsUsers = new Array();
    var self = this;
    var i = 0;
    this.match.users.forEach(function (value) {
      if(value.id != self.user.id)
      {
        arrIconsUsers[value.id] = arrIcons[i];
        if(i!=3)
          i++;
      }
    });
    this.match.map.municipalities.forEach(function (value) {
      let lat = value.centroide.lon;
      let lon = value.centroide.lat;
      let marker: L.Marker<any>; 

      if(value.owner.id == self.user.id)
        marker = L.marker([Number(lon), Number(lat)]).addTo(map);
      else
      {
        marker = L.marker([Number(lon), Number(lat)], {icon: arrIconsUsers[value.owner.id]}).addTo(map);
      }

      self.markers.push(marker);
      self.municipalities.getMunicipalitesPhoto(value).subscribe(
        result => {
          console.log(result)
          marker.bindPopup(self.popupService.makeMunicipalitesPopup(value,result?.hits[0]?.webformatURL));
        },
        err => {console.log(err);}
      );
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