import { Centroide } from './centroide';
import { User } from './user';

export class Municipality {
    public blocked: boolean;
    public centroide: Centroide;
    public elevation: string;
    public gauchoQty: string;
    public id: string;
    public nombre: string;
    public owner: User;
}