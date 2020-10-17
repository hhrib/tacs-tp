import { Injectable } from '@angular/core';
import { MatchResponse } from '../models/match.response';
import { Municipality } from '../models/municipality';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class PopUpService {

  constructor(private match : MatchResponse, public user: User) { }

  makeMunicipalitesPopup(data: Municipality): string {
    let propio = data.owner.id == this.user.id? ("(propio)"):"";
    return `` +
      `<div>Nombre: ${ data.nombre + propio }</div>` +
      `<div>Usuario: ${ data.owner.username}</div>`;
  }
}