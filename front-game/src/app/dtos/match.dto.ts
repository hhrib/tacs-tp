export class MatchDTO {
    //TODO: Ver qué campos se le agregan para la consulta

    public id: any; // --> Necesario por la base
    public prov_id: number;
    public cant_municipios: number;
    public jugadores: Array<any>; //Cuando tengamos mongo, esto debería ser un array con id de usuarios, para referir a una colección aparte de usuarios.
}