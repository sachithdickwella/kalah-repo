package com.backbase.kalah.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Stereo type {@link Configuration} class to setup and bind Redis related configurations in to the
 * {@link org.springframework.context.ApplicationContext}.
 *
 * @author Sachith Dickwella
 */
@EnableRedisRepositories
@Configuration
public class RedisConfig {

    /**
     * Create a new instance of {@link LettuceConnectionFactory} to use with {@link RedisTemplate} instance to
     * establish connectivity with Redis node.
     *
     * Decorated with {@link Primary} annotation to make this bean registration the default one hence
     * auto-configuration available in the maven dependency.
     *
     * @return new instance of {@link LettuceConnectionFactory} with default connection config.
     */
    @Primary
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration());
    }

    /**
     * Create a new {@link RedisTemplate} instance to work with {@link org.springframework.data.redis.core.RedisHash}
     * in Redis repositories. Disable the {@link RedisTemplate} transaction support explicitly to enable support
     * for Redis repository.
     *
     * @param redisConnectionFactory injects from the {@link org.springframework.context.ApplicationContext}
     *                               which bound by the bean creation of {@link #redisConnectionFactory()} as
     *                               primary connection factory.
     * @return instance of {@link RedisTemplate} to work with Redis repository.
     */
    @Bean
    public RedisTemplate<byte[], byte[]> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final var template = new RedisTemplate<byte[], byte[]>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(false);

        return template;
    }
}