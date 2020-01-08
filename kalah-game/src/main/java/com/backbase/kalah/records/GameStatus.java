package com.backbase.kalah.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;

/**
 * @author Sachith Dickwella
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStatus {

    /**
     * Id of the game.
     */
    private Long id;
    /**
     * URL to the game.
     */
    private String url;

    /**
     * Start the build process of the instance by returning a {@link Builder} instance.
     *
     * @return an instance of {@link Builder} class.
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Enforce the builder patter for {@link GameStatus} class.
     */
    public static class Builder {

        /**
         * Instance of {@link GameStatus} class to build.
         */
        private GameStatus gameStatus;

        /**
         * Initialize the {@link GameStatus} instance in the default constructor.
         */
        public Builder() {
            this.gameStatus = new GameStatus();
        }

        /**
         * Set the {@code id} variable value.
         *
         * @return the {@code this} {@link Builder} instance.
         */
        @NotNull
        public Builder id(@NotNull Long id) {
            this.gameStatus.id = id;
            return this;
        }

        /**
         * Set the {@code url} variable value.
         *
         * @return the {@code this} {@link Builder} instance.
         */
        @NotNull
        public Builder url(String url) {
            this.gameStatus.url = url;
            return this;
        }

        /**
         * Complete the builder flow by returning the instance of {@link GameStatus}.
         *
         * @return the instance of {@link GameStatus} created.
         */
        @NotNull
        public GameStatus build() {
            return gameStatus;
        }
    }

    /**
     *
     */
    public Long getId() {
        return this.id;
    }

    /**
     *
     */
    public String getURL() {
        return this.url;
    }
}
