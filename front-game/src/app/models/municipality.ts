import State from 'ol/source/State';
import { Centroide } from './centroide';
import { StateMunicipality } from './stateMunicipality';
import { User } from './user';

export class Municipality {
    public blocked: boolean;
    public centroide: Centroide;
    public elevation: string;
    public gauchosQty: string;
    public id: string;
    public nombre: string;
    public owner: User;
    public state: StateMunicipality;
}