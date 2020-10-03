import { Injectable } from '@angular/core';
import { MatchResponse } from '../models/match.response';
import { Municipality } from '../models/municipality';

@Injectable({
  providedIn: 'root'
})
export class PopUpService {

  constructor(private match : MatchResponse) { }

  makeMunicipalitesPopup(data: Municipality): string {
    let propio = data.owner.id == this.match.users[0].id? ("(propio)"):"";
    return `` +
      `<div>Nombre: ${ data.nombre + propio }</div>` +
      `<div>Usuario: ${ data.owner.username}</div>`;
  }
}