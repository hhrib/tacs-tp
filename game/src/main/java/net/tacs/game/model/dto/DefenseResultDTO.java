package net.tacs.game.model.dto;

public class DefenseResultDTO {
    public int result;
    public int gauchosAtkRestantes;
    public int gauchosDefRestantes;

    public DefenseResultDTO(int result, int gauchosAtkRestantes, int gauchosDefRestantes)
    {
        this.result = result;
        this.gauchosAtkRestantes = gauchosAtkRestantes;
        this.gauchosDefRestantes = gauchosDefRestantes;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getGauchosAtkRestantes() {
        return gauchosAtkRestantes;
    }

    public void setGauchosAtkRestantes(int gauchosAtkRestantes) {
        this.gauchosAtkRestantes = gauchosAtkRestantes;
    }

    public int getGauchosDefRestantes() {
        return gauchosDefRestantes;
    }

    public void setGauchosDefRestantes(int gauchosDefRestantes) {
        this.gauchosDefRestantes = gauchosDefRestantes;
    }
}
