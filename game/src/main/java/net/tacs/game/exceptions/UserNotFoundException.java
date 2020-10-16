package net.tacs.game.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String player_id) {
        super("User: " + player_id + "not found");
    }
}
