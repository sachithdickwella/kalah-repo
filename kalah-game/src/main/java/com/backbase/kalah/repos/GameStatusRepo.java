package com.backbase.kalah.repos;

import com.backbase.kalah.records.GameStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The Spring Data Repository interface to deal with the Redis persistence module.
 * Only the inbuilt functions needed to use to cater te requirement.
 *
 * @author Sachith Dickwella
 */
@Repository
public interface GameStatusRepo extends CrudRepository<GameStatus, Long> {
    /*
     * Nothing goes here.
     */
}
