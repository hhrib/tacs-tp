package net.tacs.game.services.impl;

import net.tacs.game.GameApplication;
import net.tacs.game.controller.MatchController;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.services.MatchService;
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
import java.util.stream.Collectors;

import net.tacs.game.model.ApiError;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;

import static net.tacs.game.GameApplication.*;

@Service("matchService")
//@Transactional
public class MatchServiceImpl implements MatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

//    @Autowired
//    private MatchRepository matchRepository;
//
//    @Autowired
//    private UserRepository userRepository;
    @Autowired
    private MunicipalityService municipalityService;

    @Override
    public List<Match> findAll() {
        List<Match> matches = GameApplication.getMatches();
        return matches;
    }

    @Override
    public Match getMatchById(String id) throws MatchException {
        Long idLong;
        if (id == null || id.isEmpty()) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("MATCH_ID_EMPTY", "Must provide an id")));
        }
        try {
            idLong = Long.valueOf(id);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid match number id", e);
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("MATCH_ID_INVALID", "Must provide a valid id")));
        }
        Optional<Match> matchToRetrieve = GameApplication.getMatches().stream().filter(match -> match.getId().equals(idLong)).findFirst();
        return matchToRetrieve.orElseThrow(() -> new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError("MATCH_NOT_FOUND", "Match not found for provided id"))));
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
    public Match createMatch(CreateMatchBean newMatchBean) throws MatchException {

        Match newMatch = validateAndGenerateMatch(newMatchBean);

        newMatch.setDate(LocalDateTime.now());
        newMatch.setState(MatchState.CREATED);
        LOGGER.info(newMatchBean.toString());

        //TODO guardar en base de datos.
        addMatch(newMatch);
        //Quizás no recibamos un Match entero, quizás recibamos el Bean de creación (dependiendo lo que elijamos)
        //en ese caso va a haber que hacer la lógica de buscar usuarios y provincia y luego crear el objeto match para
        //persistir

        return newMatch;
    }

    private Match validateAndGenerateMatch(CreateMatchBean newMatchBean) throws MatchException {
        List<ApiError> errors = new ArrayList<>();
        //TODO Algunas de estas validaciones se pueden hacer con annotations en los models. Revisar.
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
        for(long aId : newMatchBean.getUserIds())
        {
            boolean bUserFound = false;

            for(User aUser : getUsers())
            {
                if(aUser.getId() == aId)
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

        Province newProvince = new Province();

        //TODO Buscar en base de datos
        for (Province aProvince: getProvinces()) {
            if(aProvince.getId() == (newMatchBean.getProvinceId()))
            {
                //Copia de Provincia
                newProvince.setNombre(aProvince.getNombre());

                if (newMatchBean.getMunicipalitiesQty() < aProvince.getMunicipalities().size()) {
                    errors.add(new ApiError("EXCEEDED_MUNICIPALITIES_LIMIT",
                            "Amount of municipalities selected exceeds amount of province's amount of municipalities"));

                    throw new MatchException(HttpStatus.BAD_REQUEST, errors);
                }

                Random random = new Random();
                List<Municipality> tempMunicipalities = aProvince.getMunicipalities();
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

                    //TODO Gauchos son los mismos para todos?
                    muni.setGauchosQty(tempMunicipalities.get(selectedMuniIndex).getGauchosQty());

                    //TODO Por defecto se pone en defensa?
                    muni.setState(MunicipalityState.DEFENSE);

                    newProvince.addMunicipality(muni);
                }

                newMatch.setMap(newProvince);

                //asignar municipalidades a usuarios
                assignMunicipalities(newMatch.getMap().getMunicipalities(), newMatch.getUsers());

                return newMatch;
            }
        }

        errors.add(new ApiError("PROVINCE_NOT_FOUND",
                "Province Id does not exist"));

        throw new MatchException(HttpStatus.NOT_FOUND, errors);
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
}
