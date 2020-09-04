package net.tacs.game.exceptions;

public class MatchIsEmptyException extends RuntimeException{
    public MatchIsEmptyException(String error) {
        super(error);
    }
}
