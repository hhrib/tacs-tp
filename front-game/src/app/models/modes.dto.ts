export class ModesDTO {

    public name: string;
    public logicNums: number[];
    
    constructor(name: string, nums: number[]){
        this.name = name,
        this.logicNums = nums
    }
}