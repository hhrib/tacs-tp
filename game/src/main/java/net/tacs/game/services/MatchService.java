package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.Match;
import net.tacs.game.model.dto.CreateMatchDTO;
import net.tacs.game.model.dto.MuniStatisticsDTOResponse;

import java.util.List;

public interface MatchService {

    public List<Match> findAll();

    public List<Match> findMatchesByDate(String isoDateFrom, String isoDateTo) throws MatchException;

    public Match createMatch(CreateMatchDTO createMatchDTO) throws MatchException, InterruptedException;

    public Match getMatchById(String id) throws MatchException;

    public List<MuniStatisticsDTOResponse> getAllStatisticsForMatch(String id) throws MatchException;

    public void calculateConfigVariables(Match match);
}
