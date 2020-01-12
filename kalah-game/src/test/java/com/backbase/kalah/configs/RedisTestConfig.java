package com.backbase.kalah.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.PropertySource;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author Sachith Dickwella
 */
@PropertySource("classpath:test-config.properties")
@TestConfiguration
public class RedisTestConfig {

    /**
     * Instance of embedded {@link RedisServer}.
     */
    private final RedisServer redisServer;

    /**
     * Default constructor initialize instance variables.
     *
     * @param port which injects by the {@link org.springframework.context.ApplicationContext}.
     */
    public RedisTestConfig(@Value("#{T(Integer).parseInt('${test.spring.redis.port}')}") int port)
            throws IOException {
        this.redisServer = new RedisServer(port);
    }

    /**
     * Call back function to be invoked after bean creation is complete.
     */
    @PostConstruct
    public void init() {
        redisServer.start();
    }

    /**
     * Callback function to be invoked before bean destruction is complete.
     */
    @PreDestroy
    public void destroy() {
        redisServer.stop();
    }
}
