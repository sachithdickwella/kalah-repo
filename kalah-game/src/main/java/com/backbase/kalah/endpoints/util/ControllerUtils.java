package com.backbase.kalah.endpoints.util;

import com.backbase.kalah.repos.GameStatusRepo;
import com.backbase.kalah.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class is bound to serve the {@link com.backbase.kalah.endpoints.PlayKalahController} class
 * and other controller classes if any, by providing supplementary functions to achieve the
 * controllers'
 *
 * @author Sachith Dickwella
 */
@Component
public class ControllerUtils {

    /**
     * {@link GameStatusRepo} injectable instance.
     */
    private GameStatusRepo repo;

    /**
     * Constructor to {@link Autowired} or inject to the instance variables.
     *
     * @param repo instance of {@link GameStatusRepo} inject from the
     *             {@link org.springframework.context.ApplicationContext}
     */
    @Autowired
    public ControllerUtils(GameStatusRepo repo) {
        this.repo = repo;
    }

    /**
     * Select random {@link Long} integer value with checking the availability of the value
     * from the persistence store.
     *
     * This function works recursively to find out a not-used unique value.
     *
     * @return unique random {@link Long} value.
     */
    public long uniqueId(final long randomId) {
        return repo.findById(randomId)
                .map(m -> uniqueId(IdGenerator.getRandomId()))
                .orElse(randomId);
    }
}
