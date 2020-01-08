package com.backbase.kalah.repos;

import com.backbase.kalah.records.GameStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sachith Dickwella
 */
@Repository
public interface GameStatusRepo extends CrudRepository<GameStatus, Long> {
}
