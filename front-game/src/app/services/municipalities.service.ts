import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Municipality } from '../models/municipality';

const MATCH_URL = environment.BASE_URL + 'municipalities';

@Injectable({
  providedIn: 'root'
})
export class MunicipalitiesService {

  constructor(private http: HttpClient) { }

public getMunicipalitiesForCreation(): Observable<any> {

  return this.http.get<any>(`${MATCH_URL}`);

}


public getMunicipalitesPhoto(data: Municipality): Observable<any> {
  //this.http.get("https://pixabay.com/api/?key=18484881-06f0c36cb201968b0204f815a&q="+this.selectedMatch.map+"+"+muni.muniName+"&image_type=photo&page=1&per_page=3")
  return this.http.get<any>(`https://pixabay.com/api/?key=18484881-06f0c36cb201968b0204f815a&q=
    ${ data.nombre.split(' ').join('+') }&image_type=photo`);  
}

}
