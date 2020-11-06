package net.tacs.game.model.dto;

import net.tacs.game.model.Municipality;

public class AttackResultDTO {
    private int result;
    private String code;
    private Municipality muniAttacking;
    private Municipality muniDefending;

    public AttackResultDTO(int result, Municipality muniAttacking, Municipality muniDefending){
        this.result = result;
        switch (this.result)
        {
            case 0:
            {
                code = "DEFEATED";
                break;
            }
            case 1:
            {
                code = "VICTORY";
                break;
            }
        }

        this.muniAttacking = muniAttacking;
        this.muniDefending = muniDefending;
    }

    public int getResult(){
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Municipality getMuniAttacking() {
        return muniAttacking;
    }

    public void setMuniAttacking(Municipality muniAttacking) {
        this.muniAttacking = muniAttacking;
    }

    public Municipality getMuniDefending() {
        return muniDefending;
    }

    public void setMuniDefending(Municipality muniDefending) {
        this.muniDefending = muniDefending;
    }
}
