package net.tacs.game.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.mapper.MatchToDTOMapper;
import net.tacs.game.model.ApiError;
import net.tacs.game.model.Match;
import net.tacs.game.model.dto.*;
import net.tacs.game.services.MatchService;
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
    public ResponseEntity<MatchDTOResponse> getMatchById(@PathVariable("id") String id) throws MatchException {
        Match match = this.matchService.getMatchById(id);
        return new ResponseEntity<>(MatchToDTOMapper.mapMatch(match), HttpStatus.OK);
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

    //User story 3
    @PostMapping(value = "/match/{id}/attack")
    public ResponseEntity<AttackResultDTO> attackMunicipalities(@PathVariable("id") String matchId, @RequestBody AttackMuniDTO attackMuniDTO) throws MatchException
    {
        AttackResultDTO resultDTO = this.matchService.attackMunis(matchId, attackMuniDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/matches/{id}/municipalities/statistics")
    public ResponseEntity<List<MuniStatisticsDTOResponse>> getAllStatistics(@PathVariable("id") String id) throws MatchException {
        List<MuniStatisticsDTOResponse> stats = this.matchService.getAllStatisticsForMatch(id);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PatchMapping("/matches/{matchId}/municipalities/{muniId}/")
    public ResponseEntity updateMunicipalityState(@PathVariable("matchId") String matchId, @PathVariable("muniId") String muniId, @RequestBody UpdateMunicipalityStateDTO dto) throws MatchException {
        this.matchService.updateMunicipalityState(matchId, muniId, dto);
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
