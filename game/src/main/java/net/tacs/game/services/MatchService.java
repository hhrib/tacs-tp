package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.model.Match;
import net.tacs.game.model.dto.CreateMatchDTO;
import net.tacs.game.model.dto.MuniStatisticsDTOResponse;
import net.tacs.game.model.dto.UpdateMunicipalityStateDTO;
import net.tacs.game.model.websocket.ChatMessage;
import net.tacs.game.model.dto.*;

import java.util.List;
import java.util.Optional;

public interface MatchService {

    public List<Match> findAll();

    public List<Match> findMatchesByDate(String isoDateFrom, String isoDateTo) throws MatchException;

    public Match createMatch(CreateMatchDTO createMatchDTO) throws MatchException, InterruptedException;

    public Match getMatchById(String id) throws MatchException;

    public List<MuniStatisticsDTOResponse> getAllStatisticsForMatch(String id) throws MatchException;

    public void assignPlayersOrder(Match newMatch);

    public void calculateConfigVariables(Match match);

    public void endTurn(ChatMessage endTurnMessage);

    public void start(String matchStringId) throws MatchException;

    public void updateMunicipalityState(String matchId, String muniId, UpdateMunicipalityStateDTO dto) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

    public void passTurn(String matchId, String playerId) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

    public void retireFromMatch(String matchId, RetireDTO retireDTO) throws MatchException;

    public void checkMatchNotStarted(Match match) throws MatchNotStartedException;

    public void checkMatchFinished(Match match) throws MatchException;

    public Optional<Match> getMatchForUserId(String userId);

}
