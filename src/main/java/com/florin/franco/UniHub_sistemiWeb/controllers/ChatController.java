package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.api.dto.ConversationDto;
import com.florin.franco.UniHub_sistemiWeb.api.dto.ChatPeerDto;
import com.florin.franco.UniHub_sistemiWeb.api.dto.MessageResponse;
import com.florin.franco.UniHub_sistemiWeb.service.MessageService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<?> conversations(@PathVariable Long userId) {
        try {
            List<ConversationDto> list = messageService.getConversations(userId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/peers/{userId}")
    public ResponseEntity<?> peers(@PathVariable Long userId) {
        try {
            List<ChatPeerDto> list = messageService.getMutualPeers(userId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/thread/{userId}/{peerId}")
    public ResponseEntity<?> thread(@PathVariable Long userId, @PathVariable Long peerId) {
        try {
            List<MessageResponse> list = messageService.getThread(userId, peerId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
