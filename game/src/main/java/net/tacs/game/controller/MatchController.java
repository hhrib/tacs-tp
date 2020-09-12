package net.tacs.game.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.ApiError;
import net.tacs.game.model.Match;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class MatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;


    @ApiOperation(value = "Buscar todas las partidas almacenadas", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful matches search"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/matches")
    public ResponseEntity<List<Match>> getAllMatches() {
        List<Match> matches =  matchService.findAll();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    //User story 2.a
    @PostMapping(value = "/matches")
    public ResponseEntity<Match> createMatch(@RequestBody CreateMatchBean matchBean) throws MatchException {
        Match newMatch = this.matchService.createMatch(matchBean);
        return new ResponseEntity<>(newMatch, HttpStatus.CREATED);
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
        return new ResponseEntity<>(ex.getApiErrors(), ex.getHttpStatus());
    }

}
