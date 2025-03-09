package com.proyectofinder.repository;

import com.proyectofinder.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para la entidad Message.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            Long senderId, Long receiverId, Long receiverId2, Long senderId2);
}
