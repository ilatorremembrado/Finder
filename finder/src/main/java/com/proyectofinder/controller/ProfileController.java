package com.proyectofinder.controller;

import java.util.Collections;
import com.proyectofinder.model.User;
import com.proyectofinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la visualización de perfiles.
 */
@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/next")
    public ResponseEntity<?> getNextProfile(Authentication authentication) {
        String email = authentication.getName();
        System.out.println("Llamada a /profiles/next para el usuario: " + email);
        User currentUser = userService.findByEmail(email);
        User nextProfile = userService.getNextProfile(currentUser);
        if (nextProfile == null) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No hay más perfiles disponibles."));
        }
        return ResponseEntity.ok(nextProfile);
    }


}
