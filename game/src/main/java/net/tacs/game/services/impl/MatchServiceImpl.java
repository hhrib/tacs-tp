package net.tacs.game.services.impl;

import net.tacs.game.GameApplication;
import net.tacs.game.controller.MatchController;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.mapper.MuniToStatsDTOMapper;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.CreateMatchDTO;
import net.tacs.game.model.dto.MuniStatisticsDTOResponse;
import net.tacs.game.model.dto.UpdateMunicipalityStateDTO;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.ProvinceService;
import net.tacs.game.services.MunicipalityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.tacs.game.GameApplication.*;
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

    @Override
    public List<Match> findAll() {
        List<Match> matches = GameApplication.getMatches();
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
            List<Match> matches = GameApplication.getMatches();

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

        //TODO guardar en base de datos.
        addMatch(newMatch);

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
        //TODO error al no recibir configs

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

            for(User aUser : getUsers())
            {
                if(aUser.getId().equals(aId))
                {
                    bUserFound = true;
                    usersInMatch.add(aUser);
                }
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

        Province newProvince = new Province();

        //TODO Buscar en base de datos
        for (Province aProvince: getProvinces()) {
            if(aProvince.getId() == (newMatchBean.getProvinceId()))
            {
                //Copia de Provincia
                newProvince.setNombre(aProvince.getNombre());

                Random random = new Random();

                List<Municipality> tempMunicipalities;

                if(aProvince.getMunicipalities().isEmpty())
                {
                    tempMunicipalities = provinceService.findMunicipios((int)aProvince.getId(), null);

                    //Se guarda en la cache
                    aProvince.setMunicipalities(tempMunicipalities);
                }
                else {
                    tempMunicipalities = aProvince.getMunicipalities();
                }

                if (newMatchBean.getMunicipalitiesQty() > aProvince.getMunicipalities().size()) {
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

                return newMatch;
            }
        }

        errors.add(new ApiError("PROVINCE_NOT_FOUND",
                "Province Id does not exist"));

        throw new MatchException(HttpStatus.NOT_FOUND, errors);
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
    public void updateMunicipalityState(String matchIdString, String muniIdString, UpdateMunicipalityStateDTO dto) throws MatchException {
        Long matchId = validateAndGetIdLong(matchIdString, "MATCH");
        Integer muniId = validateAndGetIdLong(muniIdString, "MUNICIPALITY").intValue();
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        Optional<Municipality> muniOptional = match.getMap().getMunicipalities().stream().filter(muni -> muni.getId().equals(muniId)).findFirst();
        Municipality muni = muniOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MUNICIPALITY_NOT_FOUND_CODE, MUNICIPALITY_NOT_FOUND_DETAIL))));

        muni.setState(dto.getNewState());

        matchRepository.update(match);
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

}
