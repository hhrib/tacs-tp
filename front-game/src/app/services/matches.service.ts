import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { MatchDTO } from '../models/match.dto';

const MATCH_URL = environment.BASE_URL + 'matches';


//TODO: Agregar tipos de retorno a cada función (sacar el any)!!! 
@Injectable()
export class MatchService {
    
    constructor(private http: HttpClient) { }

    public getMatches(): any {
    
    }

    public fetchMatch(matchId: any): any {
        //return this.http.get<MatchResponse>(`${MATCH_URL}/${matchId}`);
        return this.http.get<any>(`${MATCH_URL}/${matchId}`);
    }

    public createMatch(match: MatchDTO): any {
        //return this.http.post<MatchResponse>(`${MATCH_URL}/`, match);
        console.log("PEGANDOLE A LA API REST... ")
        return this.http.post<any>(`${MATCH_URL}/`, match);
    }

    public editMatch(match: MatchDTO, matchId: any = match.id): any {
       //return this.http.put<MatchResponse>(`${MATCH_URL}/${matchId}`, match);
        return this.http.put<any>(`${MATCH_URL}/${matchId}`, match);
    }

    public deleteMatch(matchId: any): any {
        return this.http.delete<any>(`${MATCH_URL}/${matchId}`);
    }
}
