package net.tacs.game.model.dto;

import net.tacs.game.model.Municipality;

public class AttackResultDTO {
    private int result;
    private Municipality muniAttacking;
    private Municipality muniDefending;

    public AttackResultDTO(int result, Municipality muniAttacking, Municipality muniDefending){
        this.result = result;
        this.muniAttacking = muniAttacking;
        this.muniDefending = muniDefending;
    }

    public int getResult(){
        return result;
    }
}
