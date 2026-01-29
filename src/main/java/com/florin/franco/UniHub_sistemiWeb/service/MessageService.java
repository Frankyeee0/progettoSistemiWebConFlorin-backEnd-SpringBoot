package com.florin.franco.UniHub_sistemiWeb.service;

import com.florin.franco.UniHub_sistemiWeb.api.dto.MessageRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.MessageResponse;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Message;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.MessageRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AppUserRepository userRepository;

    public MessageResponse sendMessage(Long senderId, MessageRequest request) {
        AppUser sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Mittente non trovato"));
        AppUser receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Destinatario non trovato"));

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(request.getContent());
        msg.setStatus(Message.Status.SENT);

        Message saved = messageRepository.save(msg);
        return new MessageResponse(saved);
    }
    public Long countUnreadMessages(Long userId) {
        return messageRepository.countUnreadMessages(userId);
    }
    public List<MessageResponse> getReceivedMessages(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return messageRepository.findByReceiverOrderByCreatedAtDesc(user)
                .stream()
                .map(MessageResponse::new)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getSentMessages(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return messageRepository.findBySenderOrderByCreatedAtDesc(user)
                .stream()
                .map(MessageResponse::new)
                .collect(Collectors.toList());
    }

    public MessageResponse markAsRead(Long messageId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Messaggio non trovato"));
        msg.setStatus(Message.Status.READ);
        Message updated = messageRepository.save(msg);
        return new MessageResponse(updated);
    }
    public void broadcastMessage(Long senderId, String content, Ruolo onlyRoleOrNull) {
        AppUser sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Mittente non trovato"));

        List<AppUser> receivers = (onlyRoleOrNull == null)
                ? userRepository.findAll()
                : userRepository.findByRole(onlyRoleOrNull);

        // non mandarlo a se stesso
        receivers = receivers.stream()
                .filter(u -> !u.getId().equals(senderId))
                .toList();

        List<Message> msgs = new ArrayList<>(receivers.size());
        for (AppUser r : receivers) {
            Message m = new Message();
            m.setSender(sender);
            m.setReceiver(r);
            m.setContent(content);
            m.setStatus(Message.Status.SENT);
            msgs.add(m);
        }

        messageRepository.saveAll(msgs);
    }
}
