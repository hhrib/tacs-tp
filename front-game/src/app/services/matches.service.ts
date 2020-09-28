import { JsonPipe } from '@angular/common';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { MatchDTO } from '../models/match.dto';

const MATCH_URL = environment.BASE_URL + 'matches';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


//TODO: Agregar tipos de retorno a cada función (sacar el any)!!! 
@Injectable()
export class MatchService {
    
    constructor(private http: HttpClient) { }

    public getMatches(): Observable<any> {
        return this.http.get<any>(`${MATCH_URL}`)
    }

    public getById(matchId: any): Observable<any> {
        //return this.http.get<MatchResponse>(`${MATCH_URL}/${matchId}`);
        return this.http.get<any>(`${MATCH_URL}/${matchId}`);
    }

    public createMatch(match: MatchDTO): Observable<any> {
        //return this.http.post<MatchResponse>(`${MATCH_URL}/`, match);
        let matchJSON = JSON.stringify(match);
        console.log(matchJSON);
        return this.http.post<any>(`${MATCH_URL}/`, matchJSON, httpOptions);
    }

    /*public editMatch(match: MatchDTO, matchId: any = match.id): any {
       //return this.http.put<MatchResponse>(`${MATCH_URL}/${matchId}`, match);
        return this.http.put<any>(`${MATCH_URL}/${matchId}`, match);
    }*/

    public deleteMatch(matchId: any): any {
        return this.http.delete<any>(`${MATCH_URL}/${matchId}`);
    }

    public getMatchMuniStatistics(matchId: any, munId: any): Observable<any> {
        return this.http.get<any>(`${MATCH_URL}/${matchId}/municipalities/${munId}`);
    }

    
    public getMatchMunicipalities(matchId: any): any {
        var result = this.http.get<any>(`${MATCH_URL}/${matchId}/municipalities`,{responseType: 'json'})
        console.log(result);
        return result;
    }
}
