package com.proyectofinder.controller;

import com.proyectofinder.model.User;
import com.proyectofinder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Controlador para gestionar el registro y login de usuarios.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registro de usuario mediante JSON (imagen en base64).
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Endpoint para iniciar sesión.
     * Se valida el usuario y se establece la autenticación en la sesión.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest, HttpServletRequest request) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            // Crear un token de autenticación (aquí solo usamos el email y una lista vacía de authorities)
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), null, new ArrayList<>());
            // Guardar la autenticación en el SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);
            // Asociar el SecurityContext a la sesión
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Credenciales inválidas");
        }
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    //revisar este metodo porque no me fio ni de mi sombra
    @PostMapping("/updateProfileImage")
    public ResponseEntity<?> updateProfileImage(@RequestBody Map<String, String> payload, Authentication authentication) {
        String newProfileImage = payload.get("profileImage");
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if(user == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }
        user.setProfileImage(newProfileImage);
        userService.updateUser(user);
        return ResponseEntity.ok("Imagen actualizada correctamente");
    }


}
