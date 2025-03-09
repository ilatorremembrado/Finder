package com.proyectofinder.controller;

import com.proyectofinder.model.User;
import com.proyectofinder.service.InteractionService;
import com.proyectofinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar likes y dislikes.
 */
@RestController
@RequestMapping("/interaction")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;
    @Autowired
    private UserService userService;

    @PostMapping("/like/{userId}")
    public ResponseEntity<?> likeUser(Authentication auth, @PathVariable Long userId) {
        String email = auth.getName();
        User currentUser = userService.findByEmail(email);
        boolean matchOccurred = interactionService.likeUser(currentUser, userId, true);
        if (matchOccurred) {
            return ResponseEntity.ok("¡Match!");
        }
        return ResponseEntity.ok("Like registrado");
    }

    @PostMapping("/dislike/{userId}")
    public ResponseEntity<?> dislikeUser(Authentication auth, @PathVariable Long userId) {
        String email = auth.getName();
        User currentUser = userService.findByEmail(email);
        interactionService.likeUser(currentUser, userId, false);
        return ResponseEntity.ok("Dislike registrado");
    }
}
