package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.ChatMessage;

import java.security.Principal;

public interface IChatService extends IService<ChatMessage> {
    void sendMessage(ChatMessage message, Principal principal);

    void sendMessageToUser(String recipient, String content);

    String getRandomEmployeeName();

}
