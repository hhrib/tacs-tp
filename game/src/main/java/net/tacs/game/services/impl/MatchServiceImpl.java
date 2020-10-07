package net.tacs.game.services.impl;

import net.tacs.game.controller.MatchController;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.mapper.MuniToStatsDTOMapper;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.*;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.model.websocket.ChatMessage;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.ProvinceRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.ProvinceService;
import net.tacs.game.services.MunicipalityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.tacs.game.constants.Constants.*;
import static net.tacs.game.constants.Constants.MUNICIPALITY_NOT_FOUND_DETAIL;

@Service("matchService")
public class MatchServiceImpl implements MatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MunicipalityService municipalityService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public List<Match> findAll() {
        List<Match> matches = matchRepository.getMatches();
        return matches;
    }

    @Override
    public Match getMatchById(String id) throws MatchException {
        Long idLong = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchToRetrieve = matchRepository.findById(idLong);
        return matchToRetrieve.orElseThrow(() -> new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));
    }

    @Override
    public List<MuniStatisticsDTOResponse> getAllStatisticsForMatch(String id) throws MatchException {
        Long idLong = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(idLong);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));
        List<Municipality> munis = match.getMap().getMunicipalities();

        return MuniToStatsDTOMapper.mapMunis(munis);

    }

    @Override
    public List<Match> findMatchesByDate(String isoDateFrom, String isoDateTo) throws MatchException {
            List<LocalDateTime> dates = validateDatesToSearch(isoDateFrom, isoDateTo);
            LocalDateTime dateFrom = dates.get(0);
            LocalDateTime dateTo = dates.get(1);
            List<Match> matches = matchRepository.getMatches();

            if (matches == null || matches.isEmpty()) {
                throw new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError("MATCHES_NOT_FOUND", "Matches not found for dates")));
            }

            List<Match> filteredMatches = matches.stream().filter(match -> (match.getDate().toLocalDate().isEqual(dateFrom.toLocalDate()) || match.getDate().toLocalDate().isAfter(dateFrom.toLocalDate())) &&
                                                                           (match.getDate().toLocalDate().isEqual(dateFrom.toLocalDate()) || match.getDate().toLocalDate().isBefore(dateTo.toLocalDate())))
                                                          .collect(Collectors.toList());

            if (filteredMatches.isEmpty()) {
                throw new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError("MATCHES_NOT_FOUND", "Matches not found for dates")));
            }
            return filteredMatches;
    }

    @Override
    public Match createMatch(CreateMatchDTO newMatchBean) throws MatchException, InterruptedException {
        Match newMatch = validateAndGenerateMatch(newMatchBean);

        newMatch.setDate(LocalDateTime.now());
        newMatch.setState(MatchState.CREATED);
        LOGGER.info(newMatchBean.toString());

        matchRepository.add(newMatch);

        return newMatch;
    }

    private Match validateAndGenerateMatch(CreateMatchDTO newMatchBean) throws MatchException, InterruptedException {
        List<ApiError> errors = new ArrayList<>();

        if (newMatchBean.getUserIds() == null || newMatchBean.getUserIds().isEmpty()) {
            errors.add(new ApiError("MATCH_EMPTY_USERS", "Match Players not specified"));
        }
        if (newMatchBean.getUserIds().size() < 2) {
            errors.add(new ApiError("MATCH_INSUFFICIENT_NUMBER_USERS", "Match needs at least 2 players"));
        }
        if (newMatchBean.getProvinceId() == null) {
            errors.add(new ApiError("MATCH_EMPTY_MAP", "Match Map not specified"));
        }

        if (!errors.isEmpty()) {
            throw new MatchException(HttpStatus.BAD_REQUEST, errors);
        }

        //Buscar Provincia, y Users en el mapa que persiste en memoria para primeras entregas:
        //Si no se encuentra alguno, se agrega error de 404 NOT_FOUND

        Match newMatch = new Match();

        List<User> usersInMatch = new ArrayList<>();

        for(String aId : newMatchBean.getUserIds())
        {
            boolean bUserFound = false;

            Optional<User> userOptional = userRepository.findById(aId);
            if(userOptional.isPresent())
            {
                bUserFound = true;
                usersInMatch.add(userOptional.get());
            }

            if(!bUserFound)
            {
                errors.add(new ApiError("USER_NOT_FOUND", "Users not found"));
                throw new MatchException(HttpStatus.NOT_FOUND, errors);
            }
        }

        newMatch.setUsers(usersInMatch);

        //crear configuracion
        MatchConfiguration newConfig = createConfig(newMatchBean);
        newMatch.setConfig(newConfig);

        //Copia de Provincia
        Province newProvince = new Province();

        //Provincia Seleccionada a copiar
        Optional<Province> provinceOptional = provinceRepository.findById(newMatchBean.getProvinceId());
        if(provinceOptional.isEmpty())
        {
            errors.add(new ApiError("PROVINCE_NOT_FOUND",
                    "Province Id does not exist"));

            throw new MatchException(HttpStatus.NOT_FOUND, errors);
        }
        else
        {
            Province selectedProvince = provinceOptional.get();

            newProvince.setNombre(selectedProvince.getNombre());
            newProvince.setCentroide(selectedProvince.getCentroide());

            Random random = new Random();

            List<Municipality> tempMunicipalities;

            if(selectedProvince.getMunicipalities().isEmpty())
            {
                tempMunicipalities = provinceService.findMunicipios(selectedProvince.getId().intValue(), null);

                //Se guarda en la cache
                selectedProvince.setMunicipalities(tempMunicipalities);
            }
            else {
                tempMunicipalities = selectedProvince.getMunicipalities();
            }

            if (newMatchBean.getMunicipalitiesQty() > selectedProvince.getMunicipalities().size()) {
                errors.add(new ApiError("EXCEEDED_MUNICIPALITIES_LIMIT",
                        "Quantity of municipalities selected exceeds province availability"));

                throw new MatchException(HttpStatus.BAD_REQUEST, errors);
            }

            List<Integer> selectedIndexes = new ArrayList<>();

            //Crear Municipalidades
            for(int i = 1; i <= newMatchBean.getMunicipalitiesQty(); i++)
            {
                Municipality muni = new Municipality();

                int selectedMuniIndex = random.nextInt(tempMunicipalities.size());
                while(selectedIndexes.contains(selectedMuniIndex))
                {
                    selectedMuniIndex = random.nextInt(tempMunicipalities.size());
                }

                muni.setNombre(tempMunicipalities.get(selectedMuniIndex).getNombre());
                selectedIndexes.add(selectedMuniIndex);

                muni.setCentroide(tempMunicipalities.get(selectedMuniIndex).getCentroide());

                muni.setElevation(municipalityService.getElevation(muni.getCentroide()));

                //TODO: LA API SOLO ME DEJA HACER 1 REQUEST POR SEGUNDO
                TimeUnit.SECONDS.sleep(1);

                muni.setGauchosQty(newMatch.getConfig().getInitialGauchos());

                //Por defecto se pone en defensa
                muni.setState(MunicipalityState.DEFENSE);

                newProvince.addMunicipality(muni);
            }

            newMatch.setMap(newProvince);

            //asignar municipalidades a usuarios
            assignMunicipalities(newMatch.getMap().getMunicipalities(), newMatch.getUsers());

            calculateConfigVariables(newMatch);

            //create players order
            assignPlayersOrder(newMatch);

            return newMatch;
        }
    }

    /**
     * @method assignPlayersOrder
     * @param newMatch
     * @description creates an order for the match and assigns the starting player
     */
    public void assignPlayersOrder(Match newMatch)
    {
        MatchConfiguration matchConfig = newMatch.getConfig();

        List<User> playersInMatch = new ArrayList<>(newMatch.getUsers());
        List<User> playersOrder = new ArrayList<>();
        int playersCounter = playersInMatch.size();

        Random random = new Random();

        for(int i = 0; i < playersCounter; i++)
        {
            int randomPlayer = random.nextInt(playersInMatch.size());

            playersOrder.add(playersInMatch.remove(randomPlayer));
        }

        //listado con el orden de los jugadores
        matchConfig.setPlayersTurns(playersOrder);

        //el jugador que comienza la partida
        newMatch.setTurnPlayer(playersOrder.get(0));
        municipalityService.produceGauchos(newMatch, playersOrder.get(0));
    }

    /**
     * @method createConfig
     * @param newMatchBean
     * @return the config class of the created match
     */
    private MatchConfiguration createConfig(CreateMatchDTO newMatchBean) {

        MatchConfiguration newConfig = new MatchConfiguration();

        if(newMatchBean.getConfigs() != null) {
            newConfig.setMultGauchosProduction(newMatchBean.getConfigs().get(0));
            newConfig.setMultGauchosDefense(newMatchBean.getConfigs().get(1));
            newConfig.setMultDistance(newMatchBean.getConfigs().get(2));
            newConfig.setMultHeight(newMatchBean.getConfigs().get(3));
            newConfig.setMultDefense(newMatchBean.getConfigs().get(4));
            newConfig.setInitialGauchos(newMatchBean.getConfigs().get(5).intValue());
        }

        return newConfig;
    }

    /**
     * @method calculateConfigMultipliers
     * @param match
     * @description calcula las variables de maxima altura y distancia
     */
    @Override
    public void calculateConfigVariables(Match match)
    {
        double MaxHeight = 0;
        double MinHeight = 100000;
        double MaxDistance = 0;
        double MinDistance = 100000;

        for (Municipality aMuni : match.getMap().getMunicipalities())
        {
            //calculate max and min heights
            if(aMuni.getElevation() > MaxHeight)
            {
                MaxHeight = aMuni.getElevation();
            }
            if(aMuni.getElevation() < MinHeight)
            {
                MinHeight = aMuni.getElevation();
            }

            //calculate distances
            List<Municipality> municipalityList = match.getMap().getMunicipalities();

            for (Municipality otherMuni: municipalityList)
            {
                if(aMuni != otherMuni)
                {
                    double distance = aMuni.getCentroide().getDistance(otherMuni.getCentroide());

                    if(distance > MaxDistance)
                    {
                        MaxDistance = distance;
                    }
                    if(distance < MinDistance)
                    {
                        MinDistance = distance;
                    }
                }
            }
        }

        match.getConfig().setMaxHeight(MaxHeight);
        match.getConfig().setMinHeight(MinHeight);
        match.getConfig().setMaxDist(MaxDistance);
        match.getConfig().setMinDist(MinDistance);
    }

    @Override
    public void start(String matchStringId) throws MatchException {
        Long matchId = validateAndGetIdLong(matchStringId, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        checkMatchFinished(match);

        match.setState(MatchState.IN_PROGRESS);
    }

    @Override
    public void updateMunicipalityState(String matchIdString, String muniIdString, UpdateMunicipalityStateDTO dto) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Long matchId = validateAndGetIdLong(matchIdString, "MATCH");
        Integer muniId = validateAndGetIdLong(muniIdString, "MUNICIPALITY").intValue();
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        checkMatchNotStarted(match);
        checkMatchFinished(match);

        Optional<Municipality> muniOptional = match.getMap().getMunicipalities().stream().filter(muni -> muni.getId().equals(muniId)).findFirst();
        Municipality muni = muniOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MUNICIPALITY_NOT_FOUND_CODE, MUNICIPALITY_NOT_FOUND_DETAIL))));

        if(!match.getTurnPlayer().equals(muni.getOwner()))
            throw new MatchNotPlayerTurnException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(PLAYER_DOESNT_HAVE_TURN_CODE, PLAYER_DOESNT_HAVE_TURN_DETAIL)));

        if(muni.isBlocked())
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MUNICIPALITY_DESTINY_BLOCKED_CODE, MUNICIPALITY_DESTINY_BLOCKED_DETAIL)));

        muni.setState(dto.getNewState());

        matchRepository.update(match);
    }

    @Override
    public void endTurn(ChatMessage endTurnMessage) {
        template.convertAndSend("/topic/turn_end", endTurnMessage);
    }
    @Override
    public void passTurn(String matchIdString, String playerId) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Long matchId = validateAndGetIdLong(matchIdString, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        checkMatchNotStarted(match);
        checkMatchFinished(match);

        //si el jugador pertence a la partida
        if(!match.userIsInMatch(playerId))
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("PLAYER_NOT_IN_MATCH", "This player doesn't belong here")));
        }

        //si el jugador tiene el turno
        if(match.getTurnPlayer().getId().equals(playerId))
        {
            List<User> playerTurns = match.getConfig().getPlayersTurns();

            User nextPlayer = match.getConfig().setNextPlayerTurn(playerId);
            match.setTurnPlayer(nextPlayer);
            municipalityService.produceGauchos(match, nextPlayer);
        }
        else //el jugador no tiene el turno
        {
            throw new MatchNotPlayerTurnException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(PLAYER_DOESNT_HAVE_TURN_CODE, PLAYER_DOESNT_HAVE_TURN_DETAIL)));
        }
    }

    private List<LocalDateTime> validateDatesToSearch(String isoDateFrom, String isoDateTo) throws MatchException {
        List<LocalDateTime> dates = new ArrayList<>();
        try {
            if (isoDateFrom == null) {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("DATE_FROM_EMPTY", "'Date from' cannot be empty")));
            }
            if (isoDateTo == null) {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("DATE_TO_EMPTY", "'Date to' cannot be empty")));
            }
            LocalDateTime dateFrom = LocalDate.parse(isoDateFrom, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
            LocalDateTime dateTo = LocalDate.parse(isoDateTo, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
            if (dateFrom.isAfter(dateTo)) {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("DATE_FROM_AFTER_DATE_TO", "'Date from' must not be after 'date to'")));
            }
            dates.add(dateFrom);
            dates.add(dateTo);
        } catch (DateTimeParseException e) {
            LOGGER.error("Error al parsear las fechas de búsqueda", e);
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("INVALID_DATES_FORMAT", "Invalid date format received")));
        }
        return dates;
    }

    private void assignMunicipalities(List<Municipality> municipalities, List<User> users)
    {
        int usersIndex = 0;
        for(Municipality aMuni : municipalities)
        {
            aMuni.setOwner(users.get(usersIndex));

            usersIndex++;

            if(usersIndex == users.size())
            {
                usersIndex = 0;
            }
        }
    }

    private Long validateAndGetIdLong(String idString, String entity) throws MatchException {
        Long idLong;
        if (idString == null || idString.isEmpty()) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(entity.concat("_ID_EMPTY"), "Must provide an id")));
        }
        try {
            idLong = Long.valueOf(idString);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid " + entity + " number id", e);
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(entity.concat("_ID_INVALID"), "Must provide a valid id")));
        }
        return idLong;
    }

    @Override
    public void retireFromMatch(String matchStringId, RetireDTO retireDTO) throws MatchException {
        Long matchId = validateAndGetIdLong(matchStringId, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        Optional<User> userOptional = userRepository.findById(retireDTO.getPlayerId());
        User user = userOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(USER_NOT_FOUND_CODE, USER_NOT_FOUND_DETAIL))));

        if(match.getUsers().contains(user))
        {
            if(match.getState().equals(MatchState.CREATED))
            {
                match.setState(MatchState.CANCELLED);
                return;
            }

            if(match.getState().equals(MatchState.FINISHED))
            {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_FINISHED_CODE, MATCH_FINISHED_DETAIL)));
            }

            //repartir municipios
            distributeMunicipalities(match, user);
        }
        else
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(PLAYER_NOT_IN_MATCH_CODE, PLAYER_NOT_IN_MATCH_DETAIL)));
        }
    }

    private void distributeMunicipalities(Match match, User retiringPlayer) {
        //si solo hay dos personas jugando, la partida termina
        if(match.getConfig().getPlayersTurns().size() == 2)
        {
            match.setState(MatchState.FINISHED);
            match.getConfig().removePlayer(retiringPlayer);
            match.setWinner(match.getConfig().getPlayersTurns().get(0));

            return;
        }

        List<Municipality> playerMunis = retiringPlayer.getMunicipalitiesOwning(match.getMap().getMunicipalities());
        match.getConfig().removePlayer(retiringPlayer);
        List<User> playerLeft = match.getConfig().getPlayersTurns();
        int playerIndex = 0;

        for (Municipality aMuni : playerMunis) {
            aMuni.setOwner(playerLeft.get(playerIndex));

            playerIndex++;
            if(playerIndex >= playerLeft.size())
                playerIndex = 0;
        }
    }

    @Override
    public void checkMatchNotStarted(Match match) throws MatchNotStartedException {
        if(match.getState().equals(MatchState.CREATED))
            throw new MatchNotStartedException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_NOT_STARTED_CODE, MATCH_NOT_STARTED_DETAIL)));
    }

    @Override
    public void checkMatchFinished(Match match) throws MatchException {
        if(match.getState().equals(MatchState.FINISHED) || match.getState().equals(MatchState.CANCELLED))
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_FINISHED_CODE, MATCH_FINISHED_DETAIL)));
    }

    @Override
    public Match getMatchForUserId(String userId) throws MatchException {
        Optional<Match> optionalMatch = matchRepository.findForUserId(userId);
        if (!optionalMatch.isPresent()) {
            throw new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_FOR_USER_CODE, MATCH_NOT_FOUND_FOR_USER_DETAIL)));
        }
        return optionalMatch.get();
    }
}
