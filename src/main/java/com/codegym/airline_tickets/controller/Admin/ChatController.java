package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.ChatHistoryResponeDTO;
import com.codegym.airline_tickets.entity.ChatMessage;
import com.codegym.airline_tickets.service.impl.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;


@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessage message, Principal principal, SimpMessageHeaderAccessor headerAccessor) {

        if (principal == null) {
            String sessionId = headerAccessor.getSessionId();
            messagingTemplate.convertAndSend(
                    "/topic/errors/" + sessionId,
                    "Bạn chưa đăng nhập. Vui lòng đăng nhập để sử dụng tính năng chat."
            );
            return;
        }

        String sender = principal.getName();
        message.setSenderName(sender);
        String recipient = chatService.assignEmployeeToSession(sender);
        message.setRecipientName(recipient);
        message.setTimestamp(LocalDateTime.now());

        // Lưu vào database
        chatService.save(message);


        messagingTemplate.convertAndSendToUser(
                recipient,
                "/queue/messages",
                message
        );
    }

    @MessageMapping("/reply")
    public void replyToCustomer(@Payload ChatMessage message, Principal principal) {
        if (principal == null) {
            System.out.println("Principal is null");
            return;
        }

        message.setTimestamp(LocalDateTime.now());
        String sender = principal.getName(); //name nhân viên
        message.setSenderName(sender);
        String recipient = chatService.findCustomerBySession(sender);
        if (recipient != null) {
            message.setRecipientName(recipient);
            //Luu vào database
            chatService.save(message);

            messagingTemplate.convertAndSendToUser(
                    recipient,
                    "/queue/messages",
                    message
            );
        }
    }

    @MessageMapping("/history")
    public void getChatHistory(Principal principal) {

        if (principal == null) {
            System.out.println("Principal is null");
            return;
        }

        String sender = principal.getName();
        List<ChatMessage> history = chatService.getAllMessageByUserName(sender);

        ChatHistoryResponeDTO chatHistoryResponeDTO = new ChatHistoryResponeDTO();
        chatHistoryResponeDTO.setHistory(history);
        chatHistoryResponeDTO.setSender(sender);
            messagingTemplate.convertAndSendToUser(
                    sender,
                    "/queue/history",
                    chatHistoryResponeDTO // Dùng DTO bao gồm history và tên người gửi
            );
    }

    @MessageMapping("/end")
    public void endChat(@Payload ChatMessage message, Principal principal) {

        String sender = principal.getName(); //name nhân viên
        message.setSenderName(sender);
        String recipient = chatService.findCustomerBySession(sender);
        String content = message.getContent();
        message.setTimestamp(LocalDateTime.now());

        if (recipient != null) {
            message.setRecipientName(recipient);
            //Luu vào database
            chatService.save(message);

            //Xoá session
            chatService.removeSession(recipient);


            messagingTemplate.convertAndSendToUser(
                    recipient,
                    "/queue/end",
                    content // chỉ gửi content
            );
        }

    }



}
