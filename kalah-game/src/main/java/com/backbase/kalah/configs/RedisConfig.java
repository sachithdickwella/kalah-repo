package com.backbase.kalah.configs;

import com.backbase.kalah.records.GameStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author Sachith Dickwella
 */
@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Primary
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration());
    }

    @Bean
    public RedisTemplate<Long, GameStatus> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final var template = new RedisTemplate<Long, GameStatus>();

        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(false);

        final var serializer = new Jackson2JsonRedisSerializer<>(GameStatus.class);
        template.setHashValueSerializer(serializer);
        template.setValueSerializer(serializer);

        return template;
    }
}