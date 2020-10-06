export class MatchDTO {
    //TODO: Ver qué campos se le agregan para la consulta

    //public id: any; // --> Necesario por la base
    public municipalitiesQty: number;
    public provinceId: number;
    public userIds: Array<number>; //Cuando tengamos mongo, esto debería ser un array con id de usuarios, para referir a una colección aparte de usuarios.
    public creationDate: Date;
    public configs: Array<number>; //Configuración con cantGauchos y números para lógica del juego.
}