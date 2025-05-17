package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT c FROM ChatMessage c WHERE c.senderName = :userName OR c.recipientName = :userName ORDER BY c.timestamp ASC")
    List<ChatMessage> getAllMessageByUserName(String userName);
}
