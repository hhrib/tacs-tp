import { Centroide } from './centroide';
import { Municipality } from './municipality';

export class Map {
    public centroide: Centroide;
    public id: string;
    public municipalities: Array<Municipality>;
    public nombre: string;
}