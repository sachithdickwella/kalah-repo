package com.backbase.kalah;

import com.backbase.kalah.records.GameStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.backbase.kalah.util.ServiceConstance.Player.PLAYER_1;

/**
 * @author Sachith Dickwella
 */
@Component
public class PlayKalah {

    /**
     *
     */
    public void makeMove(@NotNull GameStatus gameStatus, final int pitId) {
        var oldBoard = gameStatus.getBoard();
        if (gameStatus.getPlayer().equals(PLAYER_1)) {

        } else {

        }
    }
}
