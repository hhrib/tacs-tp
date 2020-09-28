import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PopUpService {

  constructor() { }

  makeMunicipalitesPopup(data: any): string {
    return `` +
      `<div>Capital: ${ data }</div>` +
      `<div>State: ${ data }</div>` +
      `<div>Population: ${ data }</div>`
  }
}