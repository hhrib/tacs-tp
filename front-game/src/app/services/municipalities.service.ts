import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const MATCH_URL = environment.BASE_URL + 'municipalities';

@Injectable({
  providedIn: 'root'
})
export class MunicipalitiesService {

  constructor(private http: HttpClient) { }

public getMunicipalitiesForCreation(): Observable<any> {

  return this.http.get<any>(`${MATCH_URL}`);

}

}
