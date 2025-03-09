package com.proyectofinder.service;

import com.proyectofinder.model.Message;
import com.proyectofinder.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio para gestionar el envío y la recuperación de mensajes.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        return messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                userId1, userId2, userId2, userId1);
    }
}
