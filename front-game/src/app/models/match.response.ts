import { Config } from './config';
import { Map } from './map';
import { User } from './user';

// global

export class MatchResponse {
    //TODO: Ver qué campos se le agregan para la consulta

    //public id: any; // --> Necesario por la base
    public config: Config;
    public date: Date;
    public id: string;
    public map: Map;
    public state: string;
    public winner: User;
    public users: Array<User>;
    public turnPlayer: User;
}