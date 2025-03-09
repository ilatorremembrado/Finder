package com.proyectofinder.repository;

import com.proyectofinder.model.Like;
import com.proyectofinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad Like.
 */
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserFromAndUserTo(User userFrom, User userTo);
}
