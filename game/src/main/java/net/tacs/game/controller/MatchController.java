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
import net.tacs.game.model.Municipality;
import net.tacs.game.model.dto.*;
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
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;

    @Autowired
    private MunicipalityService municipalityService;

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
            matches = matchService.findAll();
        } else {
            matches = matchService.findMatchesByDate(dateFrom, dateTo);
        }

        return new ResponseEntity<>(MatchToDTOMapper.mapMatches(matches), HttpStatus.OK);
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
        Match match = this.matchService.getMatchById(id);
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
        List<MuniStatisticsDTOResponse> stats = this.matchService.getAllStatisticsForMatch(id);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PatchMapping("/matches/{matchId}/start")
    public ResponseEntity updateMatchTurn(@PathVariable("matchId") String matchId) throws MatchException {
        this.matchService.start(matchId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/matches/{id}/municipalities/gauchos")
    public ResponseEntity<List<Municipality>> moveGauchos(@PathVariable("id") String id, @RequestBody MoveGauchosDTO dto) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        List<Municipality> municipalities = municipalityService.moveGauchos(id, dto);
        return new ResponseEntity<>(municipalities, HttpStatus.OK);
    }

    //User story 3
    @PostMapping(value = "/matches/{id}/municipalities/attack")
    public ResponseEntity<AttackResultDTO> attackMunicipalities(@PathVariable("id") String id, @RequestBody AttackMuniDTO attackMuniDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        AttackResultDTO resultDTO = municipalityService.attackMunicipality(id, attackMuniDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PatchMapping("/matches/{matchId}/municipalities/{muniId}/")
    public ResponseEntity updateMunicipalityState(@PathVariable("matchId") String matchId, @PathVariable("muniId") String muniId, @RequestBody UpdateMunicipalityStateDTO dto) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        this.matchService.updateMunicipalityState(matchId, muniId, dto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/matches/{matchId}/passTurn")
    public ResponseEntity updateMatchTurn(@PathVariable("matchId") String matchId, @RequestBody PassTurnDTO passTurnDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        this.matchService.passTurn(matchId, passTurnDTO.getUserId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/matches/{matchId}/retire")
    public ResponseEntity abandonMatch(@PathVariable("matchId") String matchId, @RequestBody RetireDTO retireDTO) throws MatchException {
        this.matchService.retireFromMatch(matchId, retireDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * MatchService puede arrojar MatchException ante alguna validación que no pasa. Este método lo handlea para
     * responder con el detalle del error.
     * @param ex: MatchException arrojada por el servicio ante falla de alguna validación
     * @return ResponseEntity<List<ApiError>>> Respuesta con los errores
     */
    @ExceptionHandler(MatchException.class)
    public ResponseEntity<List<ApiError>> handleException(MatchException ex) {
        //Agregar lógica si fuese necesario
        LOGGER.error("Error con ale", ex);
        LOGGER.error("Errors: " );
        return new ResponseEntity<>(ex.getApiErrors(), ex.getHttpStatus());
    }
    
    

}
