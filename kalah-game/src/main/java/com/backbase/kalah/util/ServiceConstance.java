package com.backbase.kalah.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An interface to store application level constances to avoid repetitions.
 *
 * @author Sachith Dickwella
 */
public interface ServiceConstance {

    /**
     * Pit count configurations of the board.
     */
    int PIT_COUNT = 14;
    /**
     * Initial seed count configuration for each of the pit.
     */
    int SEEDS_PER_PIT = 6;
    /**
     * Store index of each of the players. This is a 1 based index and only
     * represent the base value of each of the index.
     */
    int STORE_INDEX = 7;
    /**
     * Time to live for an object in Redis hash.
     *
     * Representing value of each number are:
     * <code>
     *      Seconds per hour = 3600
     *      Hours per day = 24
     *      Days per week = 7
     * </code>
     *
     * Technically the value of {@code TIME_TO_LIVE} is equivalent to seconds
     * per week.
     */
    long TIME_TO_LIVE = 3600L * 24 * 7;

    /**
     * Enum representation of each of the player during a game.
     */
    enum Player {
        PLAYER_1(1), PLAYER_2(2);

        /**
         * Numeric representation to active player.
         */
        private int number;

        /**
         * The private constructor for initialize {@link #number} instance variable
         * of the enum.
         *
         * @param number value for each of the enum.
         */
        Player(int number) {
            this.number = number;
        }

        /**
         * Get the numerical value of each of the player.
         *
         * @return int {@link #number} of the enum.
         */
        public int number() {
            return this.number;
        }

        /**
         * Get the first pit id of each of the player with arithmetic progression.
         *
         * @return int value of first pit id.
         */
        public int firstPit() {
            return ((number - 1) * STORE_INDEX) + 1;
        }
    }

    /**
     * Transform JSON format {@link String} instance to {@link T} type object and return.
     *
     * @param tClass to create {@link T} type object.
     * @return an instance of {@link T} type.
     */
    static <T> T jsonToObject(String json, Class<T> tClass) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, tClass);
    }
}
