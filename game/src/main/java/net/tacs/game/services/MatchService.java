package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.model.Match;
import net.tacs.game.model.dto.*;

import java.util.List;

public interface MatchService {

    public List<Match> findAll();

    public List<Match> findMatchesByDate(String isoDateFrom, String isoDateTo) throws MatchException;

    public Match createMatch(CreateMatchDTO createMatchDTO) throws MatchException, InterruptedException;

    public Match getMatchById(String id) throws MatchException;

    public List<MuniStatisticsDTOResponse> getAllStatisticsForMatch(String id) throws MatchException;

    public void assignPlayersOrder(Match newMatch);

    public void calculateConfigVariables(Match match);

    public void updateMunicipalityState(String matchId, String muniId, UpdateMunicipalityStateDTO dto) throws MatchException, MatchNotPlayerTurnException;

    public void passTurn(String matchId, String playerId) throws MatchException, MatchNotPlayerTurnException;
}
