package com.backbase.kalah.endpoints.util;

import com.backbase.kalah.PlayKalah;
import com.backbase.kalah.records.GameStatus;
import com.backbase.kalah.repos.GameStatusRepo;
import com.backbase.kalah.util.IdGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Class is bound to serve the {@link com.backbase.kalah.endpoints.PlayKalahController} class
 * and other controller classes if any, by providing supplementary functions to achieve the
 * controllers' functionality.
 *
 * @author Sachith Dickwella
 */
@Component
public class ControllerUtils {

    /**
     * {@link GameStatusRepo} injectable instance.
     */
    private GameStatusRepo repo;
    /**
     * {@link PlayKalah} injectable instance.
     */
    private PlayKalah playKalah;

    /**
     * Constructor to {@link Autowired} or inject to the instance variables.
     *
     * @param repo      instance of {@link GameStatusRepo} inject from the
     *                  {@link org.springframework.context.ApplicationContext}
     * @param playKalah instance of {@link PlayKalah} inject from the
     *                  {@link org.springframework.context.ApplicationContext}
     */
    @Autowired
    public ControllerUtils(GameStatusRepo repo, PlayKalah playKalah) {
        this.repo = repo;
        this.playKalah = playKalah;
    }

    /**
     * Create and persist a new Kalah game instance and return the {@link GameStatus.Builder#board()} not
     * initialize game instance.
     *
     * @param requestUrl instance to append with new {@link GameStatus#getId()}. Use {@link StringBuilder} instead
     *                   of {@link StringBuffer} to avoid synchronized behaviour.
     * @return plain {@link GameStatus} instance without initializing the {@link GameStatus.Builder#board()}.
     */
    public GameStatus createGame(@NotNull StringBuilder requestUrl) {
        final long randomId = uniqueId(IdGenerator.getRandomId());
        var builder = GameStatus.builder()
                .id(randomId)
                .url(requestUrl.append(String.format("/%d", randomId)).toString());

        repo.save(builder.board().build());
        return builder.build();
    }

    /**
     * Make a move on the game and persist the latest status of the game into the persistence module. Return an
     * {@link Optional} instance due to the uncertainty of availability of the previously created game with the
     * given {@code gameId} parameter.
     *
     * @param gameId {@code long} game id.
     * @param pitId  {@code int} pit id.
     * @return an {@link GameStatus} instance wrapped with {@link Optional} class.
     */
    public Optional<GameStatus> moveAndSave(final long gameId, final int pitId) {
        return repo.findById(gameId)
                .map(gs -> {
                    playKalah.makeMove(gs, pitId);
                    return Optional.of(repo.save(gs));
                }).orElseGet(Optional::empty);
    }

    /**
     * Select random {@link Long} integer value with checking the availability of the value
     * from the persistence store.
     *
     * This function works recursively to find out a not-used unique value.
     *
     * @return unique random {@link Long} value.
     */
    private long uniqueId(final long randomId) {
        return repo.findById(randomId)
                .map(m -> uniqueId(IdGenerator.getRandomId()))
                .orElse(randomId);
    }
}
