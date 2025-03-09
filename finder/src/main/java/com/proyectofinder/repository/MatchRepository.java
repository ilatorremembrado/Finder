package com.proyectofinder.repository;

import com.proyectofinder.model.Match;
import com.proyectofinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para la entidad Match.
 */
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUser1OrUser2(User user1, User user2);
}
