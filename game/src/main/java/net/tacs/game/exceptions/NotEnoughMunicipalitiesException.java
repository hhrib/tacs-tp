package net.tacs.game.exceptions;

public class NotEnoughMunicipalitiesException extends RuntimeException{
    public NotEnoughMunicipalitiesException() {
        super("Amount of municipalities selected exceeds amount of province's amount of municipalities");
    }
}
