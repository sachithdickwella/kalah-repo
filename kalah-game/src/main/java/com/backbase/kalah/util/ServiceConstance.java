package com.backbase.kalah.util;

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
    String SEEDS_PER_PIT = "6";
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
}
