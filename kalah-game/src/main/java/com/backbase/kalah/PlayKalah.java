package com.backbase.kalah;

import com.backbase.kalah.records.GameStatus;
import com.backbase.kalah.util.InvalidPitUserException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.backbase.kalah.util.ServiceConstance.STORE_INDEX;
import static com.backbase.kalah.util.ServiceConstance.PIT_COUNT;

/**
 * Stereo type {@link Component} class to bind with {@link org.springframework.context.ApplicationContext}
 * to use during the game play.
 *
 * @author Sachith Dickwella
 */
@Component
public class PlayKalah {

    /**
     * Core game algorithm goes here. Given {@link GameStatus} instance coming from upstream function could
     * be a fresh game instance or old game instance started while back. Either way incoming object is coming
     * from the Redis store and update status accordingly.
     * <p>
     * Nothing return from this function as {@link GameStatus} instance pass by reference. Therefore, all the
     * changes do to the respective instance would reflect on the upstream object.
     *
     * @param gameStatus  instance of {@link GameStatus}.
     * @param pickupPitId which is the pit seeds pickup from.
     */
    public void makeMove(@NotNull GameStatus gameStatus, final int pickupPitId) {
        final var player = gameStatus.player();
        if (pickupPitId < 1 || pickupPitId > PIT_COUNT) {
            throw new InvalidPitUserException("Pit id %d is invalid", pickupPitId);
        } else if (pickupPitId < player.firstPit() || pickupPitId > player.number() * STORE_INDEX) {
            throw new InvalidPitUserException("It's the %s's turn", player);
        } else if (false/*TODO*/) {

        }

        final var board = Collections.synchronizedMap(gameStatus.getBoard());
        final int pickedUpSeeds = Integer.parseInt(board.get(pickupPitId));

        board.put(pickupPitId, "0");

        int nextPitId = pickupPitId + 1;
        for (int i = 1; i <= pickedUpSeeds; i++) {
            if (nextPitId > player.number() * STORE_INDEX) {
                nextPitId = player.firstPit();
            }

            board.put(nextPitId, String.valueOf(Integer.parseInt(board.get(nextPitId)) + 1));
            nextPitId++;
        }

        gameStatus.player(true);
    }
}
