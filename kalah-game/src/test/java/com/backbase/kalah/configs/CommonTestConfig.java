package com.backbase.kalah.configs;

import com.backbase.kalah.PlayKalah;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;

/**
 * Provision common bean instances required by the test environment.
 *
 * @author Sachith Dickwella
 */
@TestConfiguration
public class CommonTestConfig {

    /**
     * Singleton bean instance of {@link PlayKalah}.
     *
     * @return new {@link PlayKalah} instance.
     */
    @Scope(scopeName = BeanDefinition.SCOPE_SINGLETON)
    @Bean
    public PlayKalah getPlayKalahBean() {
        return new PlayKalah();
    }

    /**
     * Primary singleton bean instance of {@link MockMvc}.
     *
     * @param builder instance of {@link MockMvcBuilder} inject by context.
     * @return new instance of {@link MockMvc}.
     */
    @Primary
    @Scope
    @Bean("default")
    public MockMvc getMockMvcBean(@NotNull MockMvcBuilder builder) {
        return builder.build();
    }
}
