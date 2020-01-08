package com.backbase.kalah.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.backbase.kalah.util.ServiceConstance.PIT_COUNT;
import static com.backbase.kalah.util.ServiceConstance.SEEDS_PER_PIT;
import static java.util.AbstractMap.SimpleEntry;

/**
 * @author Sachith Dickwella
 */
@RedisHash("GameStatus")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStatus {

    /**
     * {@code id} of the game instance.
     */
    @Id
    private Long id;
    /**
     * {@code url} to the game.
     */
    private String url;
    /**
     * {@link Map} instance to represent the status of the board. Each of the key
     * represent the pit number and value represent the seeds in each pit.
     */
    @JsonProperty("status")
    private Map<Integer, String> board;

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
         * Copy constructor to copy the incoming/parametrized object's content into
         * a new instance and avoid {@link Cloneable} and {@link Object#clone()} usage.
         *
         * @param oldBuilder of its own type instance.
         */
        public Builder(@NotNull Builder oldBuilder) {
            this.gameStatus = GameStatus.builder()
                    .id(oldBuilder.gameStatus.id)
                    .url(oldBuilder.gameStatus.url)
                    .build();
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
         * Initialize the board with default configurations, which is 6 seeds per pit.
         *
         * @return the {@code this} {@link Builder} instance.
         */
        @NotNull
        public Builder board() {
            var newBuilder = new Builder(this);
            newBuilder.gameStatus.board = IntStream.rangeClosed(1, PIT_COUNT)
                    .mapToObj(index -> new SimpleEntry<>(index, SEEDS_PER_PIT))
                    .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
            return newBuilder;
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
    @SuppressWarnings("unused")
    public Long getId() {
        return this.id;
    }

    /**
     *
     */
    @SuppressWarnings("unused")
    public String getURL() {
        return this.url;
    }

    /**
     *
     */
    @SuppressWarnings("unused")
    public Map<Integer, String> getBoard() {
        return this.board;
    }
}
