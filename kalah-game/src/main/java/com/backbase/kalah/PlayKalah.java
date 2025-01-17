package com.backbase.kalah;

import com.backbase.kalah.records.GameStatus;
import com.backbase.kalah.util.InvalidPitUserException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static com.backbase.kalah.util.ServiceConstance.PIT_COUNT;
import static com.backbase.kalah.util.ServiceConstance.STORE_INDEX;

/**
 * Stereo type {@link Component} class to bind with {@link org.springframework.context.ApplicationContext}
 * to use during the game play.
 *
 * @author Sachith Dickwella
 */
@Component
public class PlayKalah {

    /**
     * Game board instance of {@link Map} type for this round.
     */
    private Map<Integer, String> board;

    /**
     * Core game algorithm goes here. Given {@link GameStatus} instance coming from upstream function could
     * be a fresh game instance or old game instance started while back. Either way incoming object is coming
     * from the Redis store and update status accordingly.
     *
     * Nothing return from this function as {@link GameStatus} instance pass by reference. Therefore, all the
     * changes do to the respective instance would reflect on the upstream object.
     *
     * @param gameStatus  instance of {@link GameStatus}.
     * @param pickupPitId which is the pit seeds pickup from.
     */
    public void makeMove(@NotNull GameStatus gameStatus, final int pickupPitId) {
        final var player = gameStatus.player();

        if (pickupPitId < 1 || pickupPitId > PIT_COUNT) {
            throw new InvalidPitUserException(gameStatus, "Pit id %d is invalid", pickupPitId);
        } else if (pickupPitId < player.firstPit() || pickupPitId > player.number() * STORE_INDEX) {
            throw new InvalidPitUserException(gameStatus, "It's the %s's turn", player);
        } else if (pickupPitId == player.number() * STORE_INDEX) {
            throw new InvalidPitUserException(gameStatus, "Cannot grab seeds from %d store", pickupPitId);
        }

        board = Collections.synchronizedMap(gameStatus.getBoard());
        final int pickedUpSeeds = grabSeeds(pickupPitId);
        if (pickedUpSeeds == 0) {
            throw new InvalidPitUserException(gameStatus, "Chosen pit %d, is empty, %s's got another chance",
                    pickupPitId, player);
        }

        boolean togglePlayer = false;
        int nextPitId = pickupPitId + 1;
        for (int i = 1; i <= pickedUpSeeds; i++) {
            if (nextPitId > player.number() * STORE_INDEX) {
                nextPitId = player.firstPit();
            }

            if (i == pickedUpSeeds) {
                togglePlayer = lastPitUpdate(gameStatus, nextPitId);
            } else {
                justPut1toPit(board, nextPitId);
            }
            nextPitId++;
        }

        checkAndUpdateWinner(gameStatus);
        gameStatus.player(togglePlayer);
    }

    /**
     * Grab seeds from the pit indicate by the parameter {@code pickupPit} value. By grabbing the seeds in a pit,
     * this function just return the number of seeds as well as empty (zero out) the pit.
     *
     * @param pickupPit {@code int} value which pit's seeds should be grabbed and return.
     * @return number seeds {@code int} value in the pit.
     */
    private int grabSeeds(final int pickupPit) {
        int seeds = Integer.parseInt(board.get(pickupPit));
        if (seeds > 0) board.put(pickupPit, "0");
        return seeds;
    }

    /**
     * Just insert new seed into the pit indicate by given {@code pitId} parameter. Just update the pit value
     * by addition one into the pit's existing value.
     *
     * @param board instance of {@link Map} instance that represent the board.
     * @param pitId {@code pitId} value indicate which pit to be updated.
     */
    private void justPut1toPit(@NotNull Map<Integer, String> board, final int pitId) {
        board.put(pitId, String.valueOf(Integer.parseInt(board.get(pitId)) + 1));
    }

    /**
     * Serve during the last pit update in a round of sowing seeds. Based on which pit becoming the last pit
     * of a round, this function updates the board {@link Map} instance's respective pits.
     *
     * If the last pit would become the store pit/kalah, then the player gets another chance to sow seeds
     * across him/her pits, but do not get any seeds from the opponent.
     *
     * Otherwise, if the last pit would become a usual pit/house of the player's own and that pit is empty
     * {@code (value = 0)}, then the player get to collect all the seeds from the opponent's exact opposite
     * pit and put them and player's own single seed into the player's store/kalah and then the opponent get
     * the chance to play.
     *
     * If the last pit is not the store/kalah of the player, or the general pit/house is not empty, then, just
     * put single seed into the pit and toggle the player to give the chance to next player.
     *
     * @param gameStatus instance of {@link GameStatus} that represent entire game.
     * @param lastPit    {@code int} value of the last pit of the round.
     * @return {@code boolean} value indicating whether player need to toggle or not.
     */
    private boolean lastPitUpdate(@NotNull GameStatus gameStatus, final int lastPit) {
        final int storeIndex = gameStatus.player().number() * STORE_INDEX;
        if (lastPit != storeIndex) {
            if (Integer.parseInt(board.get(lastPit)) == 0) {
                int opponentPit = (gameStatus.otherPlayer().number() * STORE_INDEX) -
                        (lastPit > STORE_INDEX ? lastPit - STORE_INDEX : lastPit);
                int opponentSeeds = grabSeeds(opponentPit);
                int newStoreSeedCount = Integer.parseInt(board.get(storeIndex)) + opponentSeeds + 1;

                board.put(storeIndex, String.valueOf(newStoreSeedCount));
            } else {
                justPut1toPit(board, lastPit);
            }
            return true;
        } else {
            justPut1toPit(board, lastPit);
            return false;
        }
    }

    /**
     * Check and update the winner of the game instance using the current board status passed by {@link Map}
     * instance.
     *
     * Continuously check each player's status by getting invoking at the end of each round and least one player
     * has no seeds to continue his/her round, immediately update the opponent's status by moving all the seeds
     * of the opponent to the store/kalah.
     *
     * @param gameStatus instance of {@link GameStatus} that represent entire game.
     */
    void checkAndUpdateWinner(@NotNull final GameStatus gameStatus) {
        final var player = gameStatus.player();
        board.entrySet()
                .parallelStream()
                .filter(e -> e.getKey() >= player.firstPit() && e.getKey() < player.number() * STORE_INDEX)
                .mapToInt(e -> Integer.parseInt(e.getValue()))
                .reduce(Integer::sum)
                .ifPresent(zeroSum -> {
                    if (zeroSum == 0) {
                        var otherPlayer = gameStatus.otherPlayer();
                        var otherPlayerStore = otherPlayer.number() * STORE_INDEX;
                        board.entrySet()
                                .parallelStream()
                                .filter(e -> e.getKey() >= otherPlayer.firstPit() && e.getKey() < otherPlayerStore)
                                .mapToInt(e -> grabSeeds(e.getKey()))
                                .reduce(Integer::sum)
                                .ifPresent(sum -> board.put(otherPlayerStore,
                                        String.valueOf(
                                                Integer.parseInt(board.get(otherPlayerStore)) + sum)
                                ));
                    }
                });
    }
}
