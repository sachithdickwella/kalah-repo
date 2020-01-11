package com.backbase.kalah.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * Class to store information about http response information and request details put by the invoker.
 * Work with Jackson JSON serializer during the serialization, thus decorated with {@link JsonInclude}
 * and {@link JsonIgnoreProperties} annotations to define behaviour of the instances during the serialization.
 *
 * @author Sachith Dickwella
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpErrorResponse {

    /**
     * {@link LocalDateTime} current date time instance.
     */
    private LocalDateTime timestamp;
    /**
     * Http status code of the response.
     */
    private int status;
    /**
     * Detail message for the http code.
     */
    private String error;
    /**
     * The reason message for the error response.
     */
    private String message;
    /**
     * The payload instance that causes the error.
     */
    private Object payload;

    /**
     * Default the private constructor to avoid object creations besides the {@link HttpErrorResponse.Builder}.
     */
    private HttpErrorResponse() {
        // do nothing
    }

    /**
     * Start the build process of the instance by returning a {@link HttpErrorResponse.Builder} instance.
     *
     * @return an instance of {@link HttpErrorResponse.Builder} class.
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Enforce the builder patter for {@link HttpErrorResponse} class.
     */
    public static class Builder {

        /**
         * Instance of {@link HttpErrorResponse} class to build.
         */
        private HttpErrorResponse httpErrorResponse;

        /**
         * Initialize the {@link HttpErrorResponse} instance in the default constructor.
         */
        private Builder() {
            this.httpErrorResponse = new HttpErrorResponse();
        }

        /**
         * Set the {@code timestamp} variable value.
         *
         * @return the {@code this} {@link HttpErrorResponse.Builder} instance.
         */
        public Builder timestamp() {
            this.httpErrorResponse.timestamp = LocalDateTime.now();
            return this;
        }

        /**
         * Set the {@code status} variable value.
         *
         * @return the {@code this} {@link HttpErrorResponse.Builder} instance.
         */
        public Builder status(int status) {
            this.httpErrorResponse.status = status;
            return this;
        }

        /**
         * Set the {@code error} variable value.
         *
         * @return the {@code this} {@link HttpErrorResponse.Builder} instance.
         */
        public Builder error(String error) {
            this.httpErrorResponse.error = error;
            return this;
        }

        /**
         * Set the {@code message} variable value.
         *
         * @return the {@code this} {@link HttpErrorResponse.Builder} instance.
         */
        public Builder message(String message) {
            this.httpErrorResponse.message = message;
            return this;
        }

        /**
         * Set the {@code payload} variable value.
         *
         * @return the {@code this} {@link HttpErrorResponse.Builder} instance.
         */
        public Builder payload(Object payload) {
            this.httpErrorResponse.payload = payload;
            return this;
        }

        /**
         * Complete the builder flow by returning the instance of {@link HttpErrorResponse}.
         *
         * @return the instance of {@link HttpErrorResponse} created.
         */
        @NotNull
        public HttpErrorResponse build() {
            return httpErrorResponse;
        }
    }

    /**
     * The getter method of the {@link #timestamp} instance that use by JSON serializer.
     * No other explicit invocations are available.
     *
     * Decorated with the {@link SuppressWarnings} to ignore the "unused" warning
     * due to aforementioned no explicit invocations.
     *
     * @return {@link LocalDateTime} instance to represent the current timestamp.
     */
    @SuppressWarnings("unused")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * The getter method of the {@link #status} instance that use by JSON serializer.
     * No other explicit invocations are available.
     *
     * Decorated with the {@link SuppressWarnings} to ignore the "unused" warning
     * due to aforementioned no explicit invocations.
     *
     * @return {@code int} variable of http error code.
     */
    @SuppressWarnings("unused")
    public int getStatus() {
        return status;
    }

    /**
     * The getter method of the {@link #error} instance that use by JSON serializer.
     * No other explicit invocations are available.
     *
     * Decorated with the {@link SuppressWarnings} to ignore the "unused" warning
     * due to aforementioned no explicit invocations.
     *
     * @return {@link String} instance of descriptive http error message.
     */
    @SuppressWarnings("unused")
    public String getError() {
        return error;
    }

    /**
     * The getter method of the {@link #message} instance that use by JSON serializer.
     * No other explicit invocations are available.
     *
     * Decorated with the {@link SuppressWarnings} to ignore the "unused" warning
     * due to aforementioned no explicit invocations.
     *
     * @return {@link String} reason message for this error response.
     */
    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    /**
     * The getter method of the {@link #payload} instance that use by JSON serializer.
     * No other explicit invocations are available.
     *
     * Decorated with the {@link SuppressWarnings} to ignore the "unused" warning
     * due to aforementioned no explicit invocations.
     *
     * @return {@link Object} instance of the current state that cause this error response.
     */
    @SuppressWarnings("unused")
    public Object getPayload() {
        return payload;
    }
}
