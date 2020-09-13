package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.Match;
import net.tacs.game.model.bean.CreateMatchBean;

import java.util.List;

public interface MatchService {

    public List<Match> findAll();

    public List<Match> findMatchesByDate(String isoDateFrom, String isoDateTo) throws MatchException;

    public Match createMatch(CreateMatchBean createMatchBean) throws MatchException;

    public Match getMatchById(String id) throws MatchException;
}
