package com.proyectofinder.service;

import com.proyectofinder.model.Like;
import com.proyectofinder.model.Match;
import com.proyectofinder.model.User;
import com.proyectofinder.repository.LikeRepository;
import com.proyectofinder.repository.MatchRepository;
import com.proyectofinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio para gestionar interacciones (like/dislike y match).
 */
@Service
public class InteractionService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean likeUser(User from, Long userToId, boolean liked) {
        Optional<User> userToOpt = userRepository.findById(userToId);
        if (!userToOpt.isPresent()) return false;
        User userTo = userToOpt.get();

        Like like = new Like(from, userTo, liked);
        likeRepository.save(like);

        if (liked) {
            Optional<Like> oppositeLike = likeRepository.findByUserFromAndUserTo(userTo, from);
            if (oppositeLike.isPresent() && oppositeLike.get().isLiked()) {
                Match match = new Match(from, userTo, LocalDateTime.now());
                matchRepository.save(match);
                return true;
            }
        }
        return false;
    }
}
