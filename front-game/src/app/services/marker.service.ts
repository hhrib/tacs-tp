import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

import * as L from 'leaflet';
import { MatchDTO } from '../models/match.dto';
import { Observable } from 'rxjs';

const MATCH_URL = environment.BASE_URL + 'matches';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class MarkerService {

  constructor(private http: HttpClient) {
  }

  makeMunicipalitiesMarkers(map: L.Map): void {
        const lat = -60.5;
        const lon = -37;
        const marker = L.marker([lon, lat]).addTo(map);

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