package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.api.dto.MessageRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.MessageResponse;
import com.florin.franco.UniHub_sistemiWeb.repository.MessageRepository;
import com.florin.franco.UniHub_sistemiWeb.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Invia un messaggio
    @PostMapping("/send/{senderId}")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable Long senderId,
            @RequestBody MessageRequest request
    ) {
        MessageResponse response = messageService.sendMessage(senderId, request);
        return ResponseEntity.ok(response);
    }

    // Messaggi ricevuti
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<MessageResponse>> getReceived(@PathVariable Long userId) {
        List<MessageResponse> messages = messageService.getReceivedMessages(userId);
        return ResponseEntity.ok(messages);
    }

    // Messaggi inviati
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MessageResponse>> getSent(@PathVariable Long userId) {
        List<MessageResponse> messages = messageService.getSentMessages(userId);
        return ResponseEntity.ok(messages);
    }

    // Segna come letto
    @PostMapping("/read/{messageId}")
    public ResponseEntity<MessageResponse> markAsRead(@PathVariable Long messageId) {
        MessageResponse response = messageService.markAsRead(messageId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/unread/{userId}")
    public UnreadCountResponse getUnreadMessages(@PathVariable Long userId) {
        Long count = messageService.countUnreadMessages(userId);
        return new UnreadCountResponse(count);
    }
    record UnreadCountResponse(Long count) {}
}
