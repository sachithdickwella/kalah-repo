package com.backbase.kalah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Starting point of the application {@code kalah} with the {@link #main(String[])}
 * method. Decorated with the {@link EnableAutoConfiguration} annotation to init
 * {@link org.springframework.context.ApplicationContext} with conventional configs.
 *
 * @author Sachith Dickwella
 */
@ComponentScan({
        "com.backbase.kalah",
        "com.backbase.kalah.configs",
        "com.backbase.kalah.endpoints",
        "com.backbase.kalah.repos"
})
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
