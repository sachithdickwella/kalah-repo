package com.backbase.kalah.util;

/**
 * The application specific {@link RuntimeException} implementation for represent specific
 * set scenarios. Being a {@code RuntimeException}, {@link InvalidPitUserException} handle
 * wrap exceptions in lambda expressions (Unchecked exception).
 *
 * @author Sachith Dickwella
 */
public class InvalidPitUserException extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InvalidPitUserException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param args    to the message to format with the {@link String}.
     */
    public InvalidPitUserException(String message, Object... args) {
        super(String.format(message, args));
    }
}
