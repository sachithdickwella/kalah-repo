package com.backbase.kalah.endpoints;

import com.backbase.kalah.endpoints.util.ControllerUtils;
import com.backbase.kalah.records.GameStatus;
import com.backbase.kalah.repos.GameStatusRepo;
import com.backbase.kalah.util.IdGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * This class/controller contains both functions for the creation of the game and make
 * the movements.
 *
 * @author Sachith Dickwella
 */
@RequestMapping("/games")
@RestController
public class PlayKalahController {

    /**
     * {@link GameStatusRepo} injectable instance.
     */
    private GameStatusRepo repo;
    /**
     * {@link ControllerUtils} injectable instance.
     */
    private ControllerUtils utils;

    /**
     * Constructor to {@link Autowired} or inject to the instance variables.
     *
     * @param repo  instance of {@link GameStatusRepo} inject from the
     *              {@link org.springframework.context.ApplicationContext}
     * @param utils instance of {@link ControllerUtils} inject from the
     *              {@link org.springframework.context.ApplicationContext}
     */
    @Autowired
    public PlayKalahController(GameStatusRepo repo, ControllerUtils utils) {
        this.repo = repo;
        this.utils = utils;
    }

    /**
     * The function for the http endpoint http://hostname:port/games and use to create new game
     * instance in memory. To keep the endpoint stateful, use Redis backed module to persist the
     * game status.
     *
     * @param request instance of {@link HttpServletRequest} injects from request bean scope.
     * @return an instance of {@link ResponseEntity} wrapping a {@link GameStatus} object.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameStatus> create(@NotNull HttpServletRequest request) {
        final long randomId = utils.uniqueId(IdGenerator.getRandomId());
        var builder = GameStatus.builder()
                .id(randomId)
                .url(String.format("%s/%d", request.getRequestURL(), randomId));

        repo.save(builder.board().build());
        return ResponseEntity.status(HttpStatus.CREATED).body(builder.build());
    }

    /**
     * The function for the endpoint http://hostname:port/games/{gameId}/pits/{pitId} to move the
     * seeds around the pits during the play. This uses that stateful {@link GameStatus} instance
     * update player and game statuses.
     *
     * @param gameId {@code long} game id from the path variable.
     * @param pitId  {@code int} pit id from the path variable.
     * @return an instance of {@link ResponseEntity} wrapping a {@link GameStatus} object.
     */
    @PutMapping(path = "/{gameId}/pits/{pitId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameStatus> move(@PathVariable("gameId") long gameId,
                                           @PathVariable("pitId") int pitId) {
        return repo.findById(gameId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
