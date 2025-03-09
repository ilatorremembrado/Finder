package com.proyectofinder.controller;

import com.proyectofinder.model.User;
import com.proyectofinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar el registro e inicio de sesión de usuarios.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint para registrar un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Endpoint para iniciar sesión.
     * Nota: La autenticación puede ser gestionada por Spring Security.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        // Lógica simplificada: validar credenciales
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Credenciales inválidas");
        }
    }
}
