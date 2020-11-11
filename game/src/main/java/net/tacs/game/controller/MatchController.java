package net.tacs.game.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.mapper.MatchToDTOMapper;
import net.tacs.game.model.ApiError;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.dto.CreateMatchDTO;
import net.tacs.game.model.dto.MatchDTOResponse;
import net.tacs.game.model.dto.MuniStatisticsDTOResponse;
import net.tacs.game.model.dto.UpdateMunicipalityStateDTO;
// import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.model.websocket.ChatMessage;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.dto.*;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.MunicipalityService;
import net.tacs.game.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.tacs.game.constants.Constants.*;


@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://tacswololo.tk"})
public class MatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;

    @Autowired
    private MunicipalityService municipalityService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "Buscar partidas", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful matches search"),
            @ApiResponse(code = 400, message = "Bad Request - Date problem"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/matches")
    public ResponseEntity<List<MatchDTOResponse>> getMatches(@RequestParam(name = "dateFrom", required = false) String dateFrom, @RequestParam(name = "dateTo", required = false) String dateTo) throws MatchException {
        List<Match> matches = new ArrayList<>();
        if (dateFrom == null && dateTo == null) {
            matches = matchRepository.findAll();
        } else {
            matches = matchService.findMatchesByDate(dateFrom, dateTo);
        }

        return new ResponseEntity<>(MatchToDTOMapper.mapMatches(matches), HttpStatus.OK);
    }

    //TODO ADMIN ONLY
    @GetMapping("/matches/statistics")
    public ResponseEntity<MatchesStatisticsDTO> getStatisticsForMatches(@RequestParam(name = "dateFrom", required = false) String dateFrom, @RequestParam(name = "dateTo", required = false) String dateTo) throws MatchException
    {
        return new ResponseEntity<>(matchService.getStatisticsForMatches(dateFrom, dateTo), HttpStatus.OK);
    }

    @ApiOperation(value = "Buscar partida por ID", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful matches search"),
            @ApiResponse(code = 400, message = "Bad Request - ID Problem"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/matches/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable("id") String id) throws MatchException {
        Long idLong = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchToRetrieve = matchRepository.findById(idLong);
        Match match = matchToRetrieve.orElseThrow(() -> new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        return new ResponseEntity<>(match, HttpStatus.OK);
    }


    public MatchController() {
        super();
    }

    //User story 2.a
    @PostMapping(value = "/matches")
    public ResponseEntity<Match> createMatch(@RequestBody CreateMatchDTO matchBean) throws MatchException, InterruptedException {
        Match newMatch = this.matchService.createMatch(matchBean);
        return new ResponseEntity<>(newMatch, HttpStatus.CREATED);
    }

    @GetMapping("/matches/{id}/municipalities/statistics")
    public ResponseEntity<List<MuniStatisticsDTOResponse>> getAllStatistics(@PathVariable("id") String id) throws MatchException {
        Long idLong = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchToRetrieve = matchRepository.findById(idLong);
        Match match = matchToRetrieve.orElseThrow(() -> new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        List<MuniStatisticsDTOResponse> stats = this.matchService.getAllStatisticsForMatch(match);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PatchMapping("/matches/{matchId}/start")
    public ResponseEntity updateMatchTurn(@PathVariable("matchId") String matchStringId) throws MatchException {
        Long matchId = validateAndGetIdLong(matchStringId, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        this.matchService.start(match);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/matches/{id}/municipalities/gauchos")
    public ResponseEntity<List<Municipality>> moveGauchos(@PathVariable("id") String id, @RequestBody MoveGauchosDTO dto) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Long matchId = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        List<Municipality> municipalities = municipalityService.moveGauchos(match, dto);
        return new ResponseEntity<>(municipalities, HttpStatus.OK);
    }

    //User story 3
    @PostMapping(value = "/matches/{id}/municipalities/attack")
    public ResponseEntity<AttackResultDTO> attackMunicipalities(@PathVariable("id") String id, @RequestBody AttackMuniDTO attackMuniDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Long matchId = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        AttackResultDTO resultDTO = municipalityService.attackMunicipality(match, attackMuniDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PatchMapping("/matches/{matchId}/municipalities/{muniId}/")
    public ResponseEntity updateMunicipalityState(@PathVariable("matchId") String id, @PathVariable("muniId") String muniId/*, @RequestBody UpdateMunicipalityStateDTO dto*/) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Long matchId = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        this.matchService.updateMunicipalityState(match, muniId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/matches/{matchId}/passTurn")
    public ResponseEntity updateMatchTurn(@PathVariable("matchId") String id, @RequestBody PassTurnDTO passTurnDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Long matchId = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        this.matchService.passTurn(match, passTurnDTO.getUserId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/matches/{matchId}/retire")
    public ResponseEntity abandonMatch(@PathVariable("matchId") String id, @RequestBody RetireDTO retireDTO) throws MatchException {
        Long matchId = validateAndGetIdLong(id, "MATCH");
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

        this.matchService.retireFromMatch(match, retireDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/matches/users/{userId}")
    public ResponseEntity<MatchIdDTO> getMatchForUser(@PathVariable("userId") String userId) throws MatchException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(USER_NOT_FOUND_CODE, USER_NOT_FOUND_DETAIL))));

        Match match = this.matchService.getMatchForUserId(user);
        MatchIdDTO matchIdDTO = new MatchIdDTO();
        matchIdDTO.setMatchId(match.getId());

        return new ResponseEntity<>(matchIdDTO, HttpStatus.OK);
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

    /**
     * MatchService puede arrojar MatchException ante alguna validación que no pasa. Este método lo handlea para
     * responder con el detalle del error.
     * @param ex: MatchException arrojada por el servicio ante falla de alguna validación
     * @return ResponseEntity<List<ApiError>>> Respuesta con los errores
     */
    @ExceptionHandler(MatchException.class)
    public ResponseEntity<List<ApiError>> handleException(MatchException ex) {
        return new ResponseEntity<>(ex.getApiErrors(), ex.getHttpStatus());
    }

    @ExceptionHandler(MatchNotStartedException.class)
    public ResponseEntity<List<ApiError>> handleException(MatchNotStartedException ex) {
        return new ResponseEntity<>(ex.getApiErrors(), ex.getHttpStatus());
    }

    @ExceptionHandler(MatchNotPlayerTurnException.class)
    public ResponseEntity<List<ApiError>> handleException(MatchNotPlayerTurnException ex) {
        return new ResponseEntity<>(ex.getApiErrors(), ex.getHttpStatus());
    }
}
