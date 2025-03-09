package com.proyectofinder.service;

import com.proyectofinder.model.Like;
import com.proyectofinder.model.User;
import com.proyectofinder.repository.LikeRepository;
import com.proyectofinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones relacionadas con el usuario.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LikeRepository likeRepository;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User getNextProfile(User currentUser) {
        // Retorna el primer usuario distinto al actual
//        return userRepository.findAll().stream()
//                .filter(u -> !u.getEmail().equals(currentUser.getEmail()))
//                .findFirst()
//                .orElse(null);
        // Obtener todos los usuarios (o bien, podrías definir una consulta específica)
        // Se usa PageRequest.of(0, 1) para obtener solo 1 registro
        Page<User> page = userRepository.findNextProfiles(currentUser.getEmail(), PageRequest.of(0, 1));
        if (page.hasContent()) {
            return page.getContent().get(0);
        }
        return null;
//        List<User> users = userRepository.findAll();
//        for (User candidate : users) {
//            // No mostrar el perfil del propio usuario
//            if (candidate.getEmail().equals(currentUser.getEmail())) {
//                continue;
//            }
//            // Verificar si ya existe una evaluación del usuario actual hacia este candidato
//            Optional<Like> evaluation = likeRepository.findByUserFromAndUserTo(currentUser, candidate);
//            if (!evaluation.isPresent()) {
//                return candidate;
//            }
//        }
//        return null;
    }
}
