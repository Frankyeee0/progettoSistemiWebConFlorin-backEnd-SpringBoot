package com.florin.franco.UniHub_sistemiWeb.api.dto;

import com.florin.franco.UniHub_sistemiWeb.entity.Message;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponse {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String receiverUsername;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
        this.senderUsername = message.getSender().getUsername();
        this.receiverId = message.getReceiver().getId();
        this.receiverUsername = message.getReceiver().getUsername();
        this.content = message.getContent();
        this.status = message.getStatus().name();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }
}
