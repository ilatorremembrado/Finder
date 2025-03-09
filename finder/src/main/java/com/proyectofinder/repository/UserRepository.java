package com.proyectofinder.repository;

import com.proyectofinder.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Devuelve los usuarios cuyo email sea distinto del actual y que no hayan sido evaluados (like o dislike)
    @Query("SELECT u FROM User u " +
            "WHERE u.email <> :currentEmail " +
            "AND u NOT IN (" +
            "SELECT l.userTo FROM Like l WHERE l.userFrom.email = :currentEmail" +
            ") " +
            "AND u NOT IN (" +
            "SELECT l.userFrom FROM Like l WHERE l.userTo.email = :currentEmail AND l.liked = false" +
            ")")
    Page<User> findNextProfiles(@Param("currentEmail") String currentEmail, Pageable pageable);
}
