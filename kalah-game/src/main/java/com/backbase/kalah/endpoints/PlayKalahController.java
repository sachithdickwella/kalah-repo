package com.backbase.kalah.endpoints;

import com.backbase.kalah.records.GameStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * This class/controller contains both functions for the creation of the game
 * and make the movement.
 *
 * @author Sachith Dickwella
 */
@RequestMapping("/games")
@RestController
public class PlayKalahController {

    /**
     * @return an instance of {@link ResponseEntity} wrapping a {@link GameStatus} object.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameStatus> create(@NotNull HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GameStatus.builder()
                        .id(1L)
                        .url(String.format("%s/%d", request.getRequestURL(), 1))
                        .build());
    }

    /**
     * @return an instance of {@link ResponseEntity} wrapping a {@link GameStatus} object.
     */
    @PutMapping(path = "/{gameId}/pits/{pitId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameStatus> move(@PathVariable("gameId") long gameId,
                                           @PathVariable("pitId") int pitId,
                                           @NotNull HttpServletRequest request) {
        return ResponseEntity.ok(GameStatus.builder()
                .id(gameId)
                .url(String.format("%s/%d", request.getRequestURL(), gameId))
                .build());
    }
}
