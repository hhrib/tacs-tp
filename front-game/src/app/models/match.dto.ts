export class MatchDTO {
    //TODO: Ver qué campos se le agregan para la consulta

    public id: any; // --> Necesario por la base
    public prov: string;
    public muni_quantity: number;
    public players: Array<string>; //Cuando tengamos mongo, esto debería ser un array con id de usuarios, para referir a una colección aparte de usuarios.
}