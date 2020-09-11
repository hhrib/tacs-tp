package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.Match;
import net.tacs.game.model.bean.CreateMatchBean;

import java.util.List;

public interface MatchService {

    public List<Match> findAll();

    public Match createMatch(CreateMatchBean createMatchBean) throws MatchException;

}
