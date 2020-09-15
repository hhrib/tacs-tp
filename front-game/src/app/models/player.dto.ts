export class PlayerDTO {
    public id : number
    public name : string
    
    //TODO: Quitar constructor innecesario. Se crearan a partir del login.
    constructor(id, name){
        this.id = id,
        this.name = name
    }

}