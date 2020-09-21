import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const MATCH_URL = environment.BASE_URL + 'provinces';

@Injectable({
  providedIn: 'root'
})
export class ProvincesService {

  constructor(private http: HttpClient) { }

public getProvincesForCreation(): Observable<any> {

  return this.http.get<any>(`${MATCH_URL}`);

}

}
