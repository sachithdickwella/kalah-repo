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

import static com.backbase.kalah.util.ServiceConstance.*;
import static java.util.AbstractMap.SimpleEntry;

/**
 * This class represent the entire game status as well as the {@link #id} and the
 * {@link #url} values of the game instance.
 *
 * Implemented the builder pattern to instance creation and management in memory.
 * The objects crete with the class are mutable hence ability to change internal
 * status of its {@link #board} attribute. This attribute represents the entire
 * <b>Kalah</b> board status and represent the latest board configuration by integer
 * numbers.
 *
 * Decorated with Jackson annotations like {@link JsonInclude} in order to exclude
 * {@code null} values from the serialized object and {@link JsonIgnoreProperties}
 * to exclude unknown attributes from the serialized object or deserialization of
 * unknown attributes from an incoming serialized object whenever this class used
 * as an input type.
 *
 * The {@link RedisHash} annotation marks the Object to be aggregate root in Redis
 * hash.
 *
 * @author Sachith Dickwella
 */
@RedisHash(value = "GameStatus", timeToLive = TIME_TO_LIVE)
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
         * Initialize the board with default configurations, which is 6 seeds per pit and 0
         * seeds for each of the stores.
         *
         * @return the {@code this} {@link Builder} instance.
         */
        @NotNull
        public Builder board() {
            var newBuilder = new Builder(this);
            newBuilder.gameStatus.board = IntStream.rangeClosed(1, PIT_COUNT)
                    .mapToObj(index -> {
                        if (index % STORE_INDEX == 0) {
                            return new SimpleEntry<>(index, "0"); // 0 (zero) value is for initial store value.
                        } else return new SimpleEntry<>(index, SEEDS_PER_PIT);
                    })
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
     * Getter method of the {@link #id} instance that use by JSON serializer.
     * No other explicit invocations are available.
     *
     * Decorated with the {@link SuppressWarnings} to ignore the "unused" warning
     * due to aforementioned no explicit invocations.
     *
     * @return {@link Long} wrapper type instance to represent game id.
     */
    @SuppressWarnings("unused")
    public Long getId() {
        return this.id;
    }

    /**
     * Getter method of the {@link #url} instance that use by JSON serializer. No
     * explicit invocations available for this method.
     *
     * Decorated with {@link SuppressWarnings} annotation to ignore "unused" warning
     * due the aforementioned no explicit cals.
     *
     * @return {@link String} instance of the incoming request URL particularly
     * represent game instance's endpoint with the {@link #id} value.
     */
    @SuppressWarnings("unused")
    public String getURL() {
        return this.url;
    }

    /**
     * Getter method of the {@link Map} that represent the Kalah game board's current
     * status by number configurations. No other explicit invocations are available and
     * therefore decorated with {@link SuppressWarnings} to omit the warning.
     *
     * Each of the {@code key} represent the pit id and each {@code value} corresponding
     * to those keys represent the number of seeds possibly available in each pit.
     *
     * @return instance of {@link Map which represent the <b>Kalah</b> board.
     */
    @SuppressWarnings("unused")
    public Map<Integer, String> getBoard() {
        return this.board;
    }
}
