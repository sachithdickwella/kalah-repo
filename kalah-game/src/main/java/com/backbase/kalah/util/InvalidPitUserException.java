package com.backbase.kalah.util;

import org.jetbrains.annotations.NotNull;

/**
 * The application specific {@link RuntimeException} implementation for represent specific
 * set scenarios. Being a {@code RuntimeException}, {@link InvalidPitUserException} handle
 * wrap exceptions in lambda expressions (Unchecked exception).
 *
 * @author Sachith Dickwella
 */
public class InvalidPitUserException extends RuntimeException {

    /**
     * {@link Object} to hold the payload which causes this exception to occur. {@code transient}
     * to exclude the instance during the {@link InvalidPitUserException} object level serialization
     * which would never happen in this program.
     */
    private final transient Object payload;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param payload the current object status which cause this exception to occur.
     * @param message the detail message. The detail message is saved for later
     *                retrieval by the {@link #getMessage()} method.
     * @param args    the vararg {@link Object} message to format with the {@link String}.
     */
    public InvalidPitUserException(@NotNull Object payload, String message, Object... args) {
        super(String.format(message, args));
        this.payload = payload;
    }

    /**
     * The getter method to {@link #payload} instance variable.
     *
     * @return the payload {@link Object}.
     */
    public Object getPayload() {
        return this.payload;
    }
}
