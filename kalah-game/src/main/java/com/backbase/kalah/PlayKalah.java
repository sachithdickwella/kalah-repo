package com.backbase.kalah;

import com.backbase.kalah.records.GameStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static com.backbase.kalah.util.ServiceConstance.STORE_INDEX;

/**
 * TODO Add comment
 *
 * @author Sachith Dickwella
 */
@Component
public class PlayKalah {

    /**
     *
     */
    public void makeMove(@NotNull GameStatus gameStatus, final int pitId) {
        gameStatus.getBoard().entrySet()
                .stream()
                .filter(e -> e.getKey() >= gameStatus.getPlayer().firstPit()
                        && e.getKey() <= (gameStatus.getPlayer().number() * STORE_INDEX))
                .forEach(null);
    }
}
