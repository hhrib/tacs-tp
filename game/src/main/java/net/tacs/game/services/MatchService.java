package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.dto.CreateMatchDTO;
import net.tacs.game.model.dto.MuniStatisticsDTOResponse;
import net.tacs.game.model.dto.UpdateMunicipalityStateDTO;
import net.tacs.game.model.websocket.ChatMessage;
import net.tacs.game.model.dto.*;

import java.util.List;

public interface MatchService {

    public List<Match> findMatchesByDate(String isoDateFrom, String isoDateTo) throws MatchException;

    public Match createMatch(CreateMatchDTO createMatchDTO) throws MatchException, InterruptedException;

    public List<MuniStatisticsDTOResponse> getAllStatisticsForMatch(Match match);

    public MatchesStatisticsDTO getStatisticsForMatches(String isoDateFrom, String isoDateTo) throws MatchException;

    public void assignPlayersOrder(Match newMatch);

    public void calculateConfigVariables(Match match);

    public void start(Match match) throws MatchException;

    public void updateMunicipalityState(Match match, String muniId) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

    public void passTurn(Match match, String playerId) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

    public void retireFromMatch(Match match, RetireDTO retireDTO) throws MatchException;

    public Match getMatchForUserId(User user) throws MatchException;

}
