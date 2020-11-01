package net.tacs.game.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import net.tacs.game.constants.Constants;
import net.tacs.game.controller.MatchController;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.mapper.MuniToStatsDTOMapper;
import net.tacs.game.model.ApiError;
import net.tacs.game.model.Match;
import net.tacs.game.model.MatchConfiguration;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.dto.CreateMatchDTO;
import net.tacs.game.model.dto.MatchesStatisticsDTO;
import net.tacs.game.model.dto.MuniStatisticsDTOResponse;
import net.tacs.game.model.dto.NextUserTurnDTO;
import net.tacs.game.model.dto.RetireDTO;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.ProvinceRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.MunicipalityService;
import net.tacs.game.services.ProvinceService;
import net.tacs.game.services.UserService;

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
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public List<Match> findAll() {
        List<Match> matches = (List<Match>) matchRepository.findAll();
        return matches;
    }

    @Override
    public List<MuniStatisticsDTOResponse> getAllStatisticsForMatch(Match match) throws MatchException {
        List<Municipality> munis = new ArrayList<>(match.getMap().getMunicipalities().values());

        return MuniToStatsDTOMapper.mapMunis(munis);
    }

    @Override
    public List<Match> findMatchesByDate(String isoDateFrom, String isoDateTo) throws MatchException {
            List<LocalDateTime> dates = validateDatesToSearch(isoDateFrom, isoDateTo);
            LocalDateTime dateFrom = dates.get(0);
            LocalDateTime dateTo = dates.get(1);
            List<Match> matches = (List<Match>) matchRepository.findAll();

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
    public MatchesStatisticsDTO getStatisticsForMatches(String isoDateFrom, String isoDateTo) throws MatchException {
        List<Match> filteredMatches = null;

        if(isoDateFrom != null && isoDateTo != null)
            filteredMatches = findMatchesByDate(isoDateFrom, isoDateTo);
        else
            filteredMatches = findAll();

        MatchesStatisticsDTO matchesStatisticsDTO = new MatchesStatisticsDTO();

        for (Match aMatch : filteredMatches) {
            switch (aMatch.getState())
            {
                case CREATED:
                {
                    matchesStatisticsDTO.addCreatedMatched();
                    break;
                }
                case CANCELLED:
                {
                    matchesStatisticsDTO.addCancelledMatches();
                    break;
                }
                case IN_PROGRESS:
                {
                    matchesStatisticsDTO.addInProgressMatches();
                    break;
                }
                case FINISHED:
                {
                    matchesStatisticsDTO.addFinishedMatches();
                    break;
                }
            }
        }

        return matchesStatisticsDTO;
    }

    @Override
    public Match createMatch(CreateMatchDTO newMatchBean) throws MatchException, InterruptedException {
        Match newMatch = validateAndGenerateMatch(newMatchBean);

        newMatch.setDate(LocalDateTime.now());
        newMatch.setState(MatchState.CREATED);
        LOGGER.info(newMatchBean.toString());

        matchRepository.save(newMatch);

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
                tempMunicipalities = new ArrayList<>(selectedProvince.getMunicipalities().values());
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
                Municipality muni = new Municipality(newConfig.getMultDefense(), newConfig.getMultGauchosProduction(), newConfig.getMultGauchosDefense());

                int selectedMuniIndex = random.nextInt(tempMunicipalities.size());
                while(selectedIndexes.contains(selectedMuniIndex))
                {
                    selectedMuniIndex = random.nextInt(tempMunicipalities.size());
                }

                muni.setNombre(tempMunicipalities.get(selectedMuniIndex).getNombre());
                selectedIndexes.add(selectedMuniIndex);

                muni.setCentroide(tempMunicipalities.get(selectedMuniIndex).getCentroide());

                muni.setGauchosQty(newMatch.getConfig().getInitialGauchos());

                newProvince.addMunicipalityMap(muni);
            }

            newMatch.setMap(newProvince);

            //buscar elevaciones de municipios
            Map<Integer, Double> elevations = municipalityService.getElevations(new ArrayList<>(newMatch.getMap().getMunicipalities().values()));
            for(Map.Entry<Integer, Double> elevation : elevations.entrySet())
            {
                newMatch.getMap().getMunicipalities().get(elevation.getKey()).setElevation(elevation.getValue());
            }

            //TODO: LA API SOLO ME DEJA HACER 1 REQUEST POR SEGUNDO
            //TimeUnit.SECONDS.sleep(1);

            //asignar municipalidades a usuarios
            assignMunicipalities(new ArrayList<>(newMatch.getMap().getMunicipalities().values()), newMatch.getUsers());

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

        for (Map.Entry<Integer, Municipality> aMuni : match.getMap().getMunicipalities().entrySet())
        {
            //calculate max and min heights
            if(aMuni.getValue().getElevation() > MaxHeight)
            {
                MaxHeight = aMuni.getValue().getElevation();
            }
            if(aMuni.getValue().getElevation() < MinHeight)
            {
                MinHeight = aMuni.getValue().getElevation();
            }

            //calculate distances
            List<Municipality> municipalityList = new ArrayList<>(match.getMap().getMunicipalities().values());

            for (Municipality otherMuni: municipalityList)
            {
                if(aMuni.getValue() != otherMuni)
                {
                    double distance = aMuni.getValue().getCentroide().getDistance(otherMuni.getCentroide());

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
    public void start(Match match) throws MatchException {
        match.checkMatchFinished();
        match.setState(MatchState.IN_PROGRESS);
        matchRepository.save(match);
    }

    @Override
    public void updateMunicipalityState(Match match, String muniIdString) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Integer muniId = validateAndGetIdLong(muniIdString, "MUNICIPALITY").intValue();

        match.checkMatchNotStarted();
        match.checkMatchFinished();

        Optional<Municipality> muniOptional = Optional.ofNullable(match.getMap().getMunicipalities().get(muniId));
        Municipality muni = muniOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(Constants.MUNICIPALITY_NOT_FOUND_CODE, Constants.MUNICIPALITY_NOT_FOUND_DETAIL))));

        if(!match.getTurnPlayer().equals(muni.getOwner()))
            throw new MatchNotPlayerTurnException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(Constants.PLAYER_DOESNT_HAVE_TURN_CODE, Constants.PLAYER_DOESNT_HAVE_TURN_DETAIL)));

        if(muni.isBlocked())
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(Constants.MUNICIPALITY_DESTINY_BLOCKED_CODE, Constants.MUNICIPALITY_DESTINY_BLOCKED_DETAIL)));

        muni.nextState();

        matchRepository.save(match);
    }

    @Override
    public void passTurn(Match match, String playerId) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        match.checkMatchNotStarted();
        match.checkMatchFinished();

        //si el jugador pertence a la partida
        if(!match.userIsInMatch(playerId))
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("PLAYER_NOT_IN_MATCH", "This player doesn't belong here")));
        }

        //si el jugador tiene el turno
        if(match.getTurnPlayer().getId().equals(playerId))
        {
            User nextPlayer = match.getConfig().setNextPlayerTurn(playerId);
            match.setTurnPlayer(nextPlayer);
            municipalityService.produceGauchos(match, nextPlayer);
            NextUserTurnDTO endTurnSocketMessage = new NextUserTurnDTO();
            endTurnSocketMessage.setUsername(nextPlayer.getUsername());
            template.convertAndSend("/topic/" + match.getId().toString() +"/turn_end", endTurnSocketMessage);
            matchRepository.save(match);
        }
        else //el jugador no tiene el turno
        {
            throw new MatchNotPlayerTurnException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(Constants.PLAYER_DOESNT_HAVE_TURN_CODE, Constants.PLAYER_DOESNT_HAVE_TURN_DETAIL)));
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
    public void retireFromMatch(Match match, RetireDTO retireDTO) throws MatchException {
        Optional<User> userOptional = userRepository.findById(retireDTO.getPlayerId());
        User user = userOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(Constants.USER_NOT_FOUND_CODE, Constants.USER_NOT_FOUND_DETAIL))));

        if(match.getUsers().contains(user))
        {
            if(match.getState().equals(MatchState.CREATED))
            {
                match.setState(MatchState.CANCELLED);
                return;
            }

            if(match.getState().equals(MatchState.FINISHED))
            {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(Constants.MATCH_FINISHED_CODE, Constants.MATCH_FINISHED_DETAIL)));
            }

            //repartir municipios
            distributeMunicipalities(match, user);
        }
        else
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(Constants.PLAYER_NOT_IN_MATCH_CODE, Constants.PLAYER_NOT_IN_MATCH_DETAIL)));
        }
    }

    private void distributeMunicipalities(Match match, User retiringPlayer) {
        //si solo hay dos personas jugando, la partida termina
        if(match.getConfig().getPlayersTurns().size() == 2)
        {
            match.setState(MatchState.FINISHED);
            match.getConfig().removePlayer(retiringPlayer);
            match.setWinner(match.getConfig().getPlayersTurns().get(0));

            userService.setWinnerAndLosersStats(match);

            return;
        }

        List<Municipality> playerMunis = retiringPlayer.getMunicipalitiesOwning(new ArrayList<>(match.getMap().getMunicipalities().values()));
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
    public Match getMatchForUserId(String userId) throws MatchException {
        Optional<Match> optionalMatch = Optional.ofNullable(matchRepository.findByUsersId(userId));
        if (!optionalMatch.isPresent()) {
            throw new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(Constants.MATCH_NOT_FOUND_FOR_USER_CODE, Constants.MATCH_NOT_FOUND_FOR_USER_DETAIL)));
        }
        return optionalMatch.get();
    }
}
