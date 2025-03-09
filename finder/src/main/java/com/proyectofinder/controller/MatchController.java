package com.proyectofinder.controller;

import com.proyectofinder.model.Match;
import com.proyectofinder.model.User;
import com.proyectofinder.repository.MatchRepository;
import com.proyectofinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getMatches(Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        List<Match> matches = matchRepository.findByUser1OrUser2(currentUser, currentUser);
        return ResponseEntity.ok(matches);
    }
}
