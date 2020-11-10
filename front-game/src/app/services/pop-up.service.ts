import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatchResponse } from '../models/match.response';
import { Municipality } from '../models/municipality';
import { User } from '../models/user';
import { MatchService } from './matches.service';

@Injectable({
  providedIn: 'root'
})
export class PopUpService {
  pagePhoto:any;
  constructor(
    private match : MatchResponse, 
    public user: User, 
    public matchService : MatchService,
    private http: HttpClient) { }

  makeMunicipalitesPopup(data: Municipality, img: any): string {
    let propio = data.owner.id == this.user.id? ("(propio)"):"";
    return `` +
      `<div>Name: ${ data.nombre + propio }</div>` +
      `<div>User: ${ data.owner.username }</div>` +
      `<div>State: ${ data.state.name }</div>` +
      `<div>Gauchos: ${ data.gauchosQty }</div>` +
      `<img style="width:100%" src= ${ img } >`;
  }
}