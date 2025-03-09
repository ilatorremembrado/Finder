package com.proyectofinder.controller;

import com.proyectofinder.model.Message;
import com.proyectofinder.model.User;
import com.proyectofinder.service.MessageService;
import com.proyectofinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Controlador para el sistema de mensajería entre usuarios con match.
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(Authentication auth, @RequestBody Message message) {
        String email = auth.getName();
        User sender = userService.findByEmail(email);
        message.setSenderId(sender.getId());
        messageService.sendMessage(message);
        return ResponseEntity.ok(Collections.singletonMap("message", "Mensaje enviado"));
    }

    @GetMapping("/conversation/{userId}")
    public ResponseEntity<?> getConversation(Authentication auth, @PathVariable Long userId) {
        String email = auth.getName();
        User currentUser = userService.findByEmail(email);
        List<Message> conversation = messageService.getConversation(currentUser.getId(), userId);
        return ResponseEntity.ok(conversation);
    }
}
