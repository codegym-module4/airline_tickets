package com.codegym.airline_tickets.dto;


import com.codegym.airline_tickets.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChatHistoryResponeDTO {
    private String sender;
    private List<ChatMessage> history;
}
