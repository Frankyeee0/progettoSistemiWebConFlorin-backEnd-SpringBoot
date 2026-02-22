package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.api.dto.ChatMessageRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.MessageResponse;
import com.florin.franco.UniHub_sistemiWeb.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(ChatMessageRequest request) {
        MessageResponse saved = messageService.sendChatMessage(
                request.getSenderId(),
                request.getReceiverId(),
                request.getContent()
        );

        messagingTemplate.convertAndSend("/topic/chat/" + saved.getReceiverId(), saved);
        messagingTemplate.convertAndSend("/topic/chat/" + saved.getSenderId(), saved);
    }
}
