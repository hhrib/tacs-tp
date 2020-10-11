import { JsonPipe } from '@angular/common';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { MatchDTO } from '../models/match.dto';
import { MatchResponse } from '../models/match.response';

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

    public getById(matchId: any): Observable<MatchResponse> {
        return this.http.get<any>(`${MATCH_URL}/${matchId}`);
    }

    public createMatch(match: MatchDTO): Observable<MatchResponse> {
        let matchJSON = JSON.stringify(match);
        //console.log(matchJSON);
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

    public atackMatchMunicipalities(matchId: any, atack: any): any {
        let atackJSON = JSON.stringify(atack);
        console.log("Attacking...");
        console.log(atackJSON);
        console.log("End attack");
        return this.http.post<any>(`${MATCH_URL}/${matchId}/municipalities/attack/`, atackJSON, httpOptions);
    }

    public moveMatchMunicipalities(matchId: any, move: any): any {
        let moveJSON = JSON.stringify(move);
        console.log("Moving...");
        console.log(moveJSON);
        console.log("End move");
        return this.http.post<any>(`${MATCH_URL}/${matchId}/municipalities/gauchos/`, moveJSON, httpOptions);
    }

    public stateMatchMunicipalities(matchId: any, muniId: any, state: any): any {
        let stateJSON = JSON.stringify(state);
        console.log("Changing state...");
        console.log(stateJSON);
        console.log("End change state");
        return this.http.patch<any>(`${MATCH_URL}/${matchId}/municipalities/${muniId}/`, stateJSON, httpOptions);
    }

    public passTurnMatch(matchId: any, passTurn: any): any {
        let passTurnJSON = JSON.stringify(passTurn);
        console.log("Passing...");
        console.log(passTurnJSON);
        console.log("End pass");
        return this.http.patch<any>(`${MATCH_URL}/${matchId}/passTurn/`, passTurnJSON, httpOptions);
    }

    public newTurnMatch(matchId: any): any {
        return this.http.patch<any>(`${MATCH_URL}/${matchId}/start/`, null, {responseType: 'json'});
    }
}
