package com.florin.franco.UniHub_sistemiWeb.api.dto;

import java.time.LocalDateTime;

public class ConversationDto {
    private Long peerId;
    private String peerUsername;
    private String peerImage;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public Long getPeerId() {
        return peerId;
    }

    public void setPeerId(Long peerId) {
        this.peerId = peerId;
    }

    public String getPeerUsername() {
        return peerUsername;
    }

    public void setPeerUsername(String peerUsername) {
        this.peerUsername = peerUsername;
    }

    public String getPeerImage() {
        return peerImage;
    }

    public void setPeerImage(String peerImage) {
        this.peerImage = peerImage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}
