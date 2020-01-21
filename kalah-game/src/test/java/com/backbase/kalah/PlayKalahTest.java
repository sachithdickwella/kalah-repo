package com.backbase.kalah;

import com.backbase.kalah.records.GameStatus;
import com.backbase.kalah.util.InvalidPitUserException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.AbstractMap;
import java.util.Set;

import static com.backbase.kalah.util.ServiceConstance.PIT_COUNT;
import static com.backbase.kalah.util.ServiceConstance.Player.PLAYER_1;
import static com.backbase.kalah.util.ServiceConstance.Player.PLAYER_2;
import static com.backbase.kalah.util.ServiceConstance.STORE_INDEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test class for {@link PlayKalah} bean instance to make sure the behaviour.
 * This is the core class of the game that provision the game's requirement.
 *
 * @author Sachith Dickwella
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = PlayKalah.class)
public class PlayKalahTest {

    /**
     * Instance of {@link PlayKalah} to be tested.
     */
    @Autowired
    private PlayKalah playKalah;

    /**
     * Instance of {@link GameStatus} to test on.
     */
    private static GameStatus gameStatus;
    /**
     * {@link Integer} type immutable {@link Set} of PLAYER_1's pit ids.
     */
    private final Set<Integer> player1Pits = Set.of(1, 2, 3, 4, 5, 6, 7);
    /**
     * {@link Integer} type immutable {@link Set} of PLAYER_2's pit ids.
     */
    private final Set<Integer> player2Pits = Set.of(8, 9, 10, 11, 12, 13, 14);

    /**
     * Setup the {@link GameStatus} initial instance to test on.
     */
    @BeforeAll
    public static void setup() {
        gameStatus = GameStatus.builder()
                .id(1L)
                .url("http://localhost:8080/games/1")
                .board()
                .build();
    }

    /**
     * Test the status of initial game board status before game starts.
     */
    @Order(1)
    @Test
    @DisplayName("Initial game board status check")
    public void testOriginalBoardStatus() {
        assertNotNull("'gameStatus' instance is null", gameStatus);
        gameStatus.getBoard().forEach((k, v) -> {
            if (k % STORE_INDEX == 0) {
                assertEquals("Seed count invalid in store", 0, Integer.parseInt(v));
            } else {
                assertEquals("Seed count invalid", 6, Integer.parseInt(v));
            }
        });
    }

    /**
     * Test the status of game board after a move from PLAYER_1.
     */
    @Order(2)
    @Test
    @DisplayName("When play PLAYER_1's second turn")
    public void testMakeMove1() {
        assertNotNull("'gameStatus' instance is null", gameStatus);

        playKalah.makeMove(gameStatus, 1);
        gameStatus.getBoard().entrySet()
                .stream()
                .filter(e -> player1Pits.contains(e.getKey()))
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), Integer.parseInt(e.getValue())))
                .forEach(e -> {
                    switch (e.getKey()) {
                        case 1:
                            assertEquals("Seed count is invalid in 1st pit", 0, (int) e.getValue());
                            break;
                        case 7:
                            assertEquals("Seed count is invalid in store", 1, (int) e.getValue());
                            break;
                        default:
                            assertEquals("Seed count is invalid", 7, (int) e.getValue());
                            break;
                    }
                });
    }

    /**
     * Test the behaviour, when the PLAYER_1 has made the last move into the store/kalah and PLAYER_2 trying
     * make a move whilst the PLAYER_1 got another chance.
     */
    @Order(3)
    @Test
    @DisplayName("Trying to give a chance to PLAYER_2 after PLAYER_1's last move on the store")
    public void testMakeMove2() {
        assertNotNull("'gameStatus' instance is null", gameStatus);

        var ex = Assertions.assertThrows(InvalidPitUserException.class, () -> playKalah.makeMove(gameStatus, 8));
        assertNotNull("Exception is expected", ex);
        assertEquals("Error message is invalid", "It's the PLAYER_1's turn", ex.getMessage());
    }

    /**
     * Test the behaviour, when any of the player provides a invalid pit id.
     */
    @Order(4)
    @Test
    @DisplayName("When the pit id is invalid : 20")
    public void testMakeMove3() {
        assertNotNull("'gameStatus' instance is null", gameStatus);

        var ex = Assertions.assertThrows(InvalidPitUserException.class, () -> playKalah.makeMove(gameStatus, 20));
        assertNotNull("Exception is expected", ex);
        assertEquals("Error message is invalid", "Pit id 20 is invalid", ex.getMessage());
    }

    /**
     * Test the behaviour, when the player tries to pickup seeds from him/her store/kalah.
     */
    @Order(5)
    @Test
    @DisplayName("When trying to pickup seeds from store/kalah pit")
    public void testMakeMove4() {
        assertNotNull("'gameStatus' instance is null", gameStatus);

        var ex = Assertions.assertThrows(InvalidPitUserException.class, () -> playKalah.makeMove(gameStatus, 7));
        assertNotNull("Exception is expected", ex);
        assertEquals("Error message is invalid", "Cannot grab seeds from 7 store", ex.getMessage());
    }

    /**
     * Test the behaviour, when the player tries to pickup seeds from an empty pit/house of their own.
     */
    @Order(6)
    @Test
    @DisplayName("When trying to pickup seeds from an empty own pit")
    public void testMakeMove5() {
        assertNotNull("'gameStatus' instance is null", gameStatus);

        var ex = Assertions.assertThrows(InvalidPitUserException.class, () -> playKalah.makeMove(gameStatus, 1));
        assertNotNull("Exception is expected", ex);
        assertEquals("Error message is invalid", "Chosen pit 1, is empty, PLAYER_1's got another chance",
                ex.getMessage());
    }

    /**
     * Test the behaviour, when PLAYER_1 takes the next chance and end up in an empty pit/house.
     */
    @Order(7)
    @Test
    @DisplayName("When play PLAYER_1's second chance, end up in empty pit and should pick opponents seeds")
    public void testMakeMove6() {
        assertNotNull("'gameStatus' instance is null", gameStatus);

        playKalah.makeMove(gameStatus, 2);

        final var board = gameStatus.getBoard();
        board.entrySet()
                .stream()
                .filter(e -> player1Pits.contains(e.getKey()))
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), Integer.parseInt(e.getValue())))
                .forEach(e -> {
                    switch (e.getKey()) {
                        case 1:
                            assertEquals("Seed count is invalid in 1st pit", 1, (int) e.getValue());
                            break;
                        case 2:
                            assertEquals("Seed count is invalid in 2nd pit", 0, (int) e.getValue());
                            break;
                        case 7:
                            assertEquals("Seed count is invalid in store", 9, (int) e.getValue());
                            break;
                        default:
                            assertEquals("Seed count is invalid", 8, (int) e.getValue());
                            break;
                    }
                });

        assertEquals("Opponent's opposit pit emptied out", 0,
                Integer.parseInt(board.get(PIT_COUNT - 2)));
    }

    /**
     * Test the behaviour, when the player end up in a not-empty pit/house.
     */
    @Order(8)
    @Test
    @DisplayName("When play PLAYER_2's first round to end in a not-empty pit/house")
    public void testMakeMove7() {
        assertNotNull("'gameStatus' instance is null", gameStatus);

        playKalah.makeMove(gameStatus, 10);

        final var board = gameStatus.getBoard();
        board.entrySet()
                .stream()
                .filter(e -> player2Pits.contains(e.getKey()))
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), Integer.parseInt(e.getValue())))
                .forEach(e -> {
                    switch (e.getKey()) {
                        case 1:
                            assertEquals("Seed count is invalid in first pit", 1, (int) e.getValue());
                            break;
                        case 10:
                            assertEquals("Seed count is invalid in 10th pits", 0, (int) e.getValue());
                            break;
                        case 12:
                            assertEquals("Seed count is invalid in 12th pits", 1, (int) e.getValue());
                            break;
                        case 14:
                            assertEquals("Seed count is invalid in store", 1, (int) e.getValue());
                            break;
                        default:
                            assertEquals("Seed count is invalid", 7, (int) e.getValue());
                            break;
                    }
                });
    }

    /**
     * Orchestrate the board make it last move and demonstrate the winning behaviour. Here in this function,
     * {@link GameStatus#getBoard()} get modified and perform an assertion test on modified board.
     */
    @Order(9)
    @Test
    @DisplayName("Test the synthetically modify the board for status")
    public void testBeforeLastMove() {
        final var board = gameStatus.getBoard();
        board.put(1, "1");
        board.put(2, "0");
        board.put(3, "0");
        board.put(4, "0");
        board.put(5, "0");
        board.put(6, "0");
        board.put(7, "42");
        board.put(8, "4");
        board.put(9, "1");
        board.put(10, "2");
        board.put(11, "4");
        board.put(12, "0");
        board.put(13, "3");
        board.put(14, "15");

        assertEquals("Total seeds count is invalid", 72, gameStatus.getBoard()
                .entrySet()
                .parallelStream()
                .mapToInt(e -> Integer.parseInt(e.getValue()))
                .sum());
    }

    /**
     * Test the behaviour of the winning move and last board status after completion.
     */
    @Order(10)
    @Test
    @DisplayName("When play PLAYER_1's last move finish the game")
    public void testLastMove() {
        playKalah.makeMove(gameStatus, 1);

        var board = gameStatus.getBoard();

        var player1Seeds = Integer.parseInt(board.get(PLAYER_1.number() * STORE_INDEX));
        assertEquals("PLAYER_1's seeds count is invalid", 43, player1Seeds);

        var player2Seeds = Integer.parseInt(board.get(PLAYER_2.number() * STORE_INDEX));
        assertEquals("PLAYER_2's seeds count is invalid", 29, player2Seeds);

        assertEquals("Total seeds count is invalid", 72, player1Seeds + player2Seeds);

        board.forEach((k, v) -> {
            if (k % 7 != 0) assertEquals("Each and every store is not empty", 0, Integer.parseInt(v));
        });
    }
}
