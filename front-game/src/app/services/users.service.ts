import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserDTO } from '../models/user.dto';

const MATCH_URL = environment.BASE_URL + 'users';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient) { }

  public getAllUsers(): Observable<any> {
    return this.http.get<any>(`${MATCH_URL}`);
  }

  public getAllUsersNotInMatch(): Observable<any> {
    return this.http.get<any>(`${MATCH_URL}/available`);
  }
}
