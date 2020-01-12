package com.backbase.kalah.util;

import java.util.Random;

/**
 * Uses to generate random integer numbers using {@link Random} class.
 *
 * @author Sachith Dickwella
 */
public interface IdGenerator {

    /**
     * {@link Random} instance to generate random integer values.
     */
    Random RANDOM = new Random();

    /**
     * Return a random {@code long} value between 0 and {@link Integer#MAX_VALUE}.
     *
     * @return {@code long} random value.
     */
    static long getRandomId() {
        return RANDOM.longs(0, Long.MAX_VALUE).findAny().orElse(0);
    }
}
