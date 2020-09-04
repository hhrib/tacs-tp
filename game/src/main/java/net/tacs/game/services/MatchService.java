package net.tacs.game.services;

import net.tacs.game.model.Match;

public interface MatchService {

    public Iterable<Match> findAll();

}
