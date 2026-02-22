package com.florin.franco.UniHub_sistemiWeb.service;

import com.florin.franco.UniHub_sistemiWeb.api.dto.ConversationDto;
import com.florin.franco.UniHub_sistemiWeb.api.dto.ChatPeerDto;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public MessageResponse sendChatMessage(Long senderId, Long receiverId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Messaggio vuoto");
        }
        AppUser sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Mittente non trovato"));
        AppUser receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Destinatario non trovato"));

        if (sender.getRole() != Ruolo.ADMIN) {
            requireAllowedChat(sender, receiver);
        }

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content.trim());
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

    public List<MessageResponse> getThread(Long userId, Long peerId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        AppUser peer = userRepository.findById(peerId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (user.getRole() != Ruolo.ADMIN) {
            requireAllowedChat(user, peer);
        }

        return messageRepository.findThread(userId, peerId)
                .stream()
                .map(MessageResponse::new)
                .collect(Collectors.toList());
    }

    public List<ConversationDto> getConversations(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<Message> messages = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
        Map<Long, ConversationDto> map = new LinkedHashMap<>();

        for (Message msg : messages) {
            AppUser peer = msg.getSender().getId().equals(userId)
                    ? msg.getReceiver()
                    : msg.getSender();
            if (map.containsKey(peer.getId())) {
                continue;
            }
            if (user.getRole() != Ruolo.ADMIN && !isAllowedChat(user, peer)) {
                continue;
            }
            ConversationDto dto = new ConversationDto();
            dto.setPeerId(peer.getId());
            dto.setPeerUsername(peer.getUsername());
            dto.setPeerImage(peer.getProfileImage());
            dto.setLastMessage(msg.getContent());
            dto.setLastMessageAt(msg.getCreatedAt());
            map.put(peer.getId(), dto);
        }

        return new ArrayList<>(map.values());
    }

    public List<ChatPeerDto> getMutualPeers(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return user.getSeguiti().stream()
                .filter(peer -> isAllowedChat(user, peer))
                .map(peer -> {
                    ChatPeerDto dto = new ChatPeerDto();
                    dto.setId(peer.getId());
                    dto.setUsername(peer.getUsername());
                    dto.setName(peer.getName());
                    dto.setSurname(peer.getSurname());
                    dto.setProfileImage(peer.getProfileImage());
                    return dto;
                })
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

    private void requireAllowedChat(AppUser sender, AppUser receiver) {
        if (!isAllowedChat(sender, receiver)) {
            throw new RuntimeException("Chat consentita solo tra utenti che si seguono o che ti seguono");
        }
    }

    private boolean isAllowedChat(AppUser sender, AppUser receiver) {
        boolean senderFollows = sender.getSeguiti().contains(receiver);
        boolean receiverFollows = receiver.getSeguiti().contains(sender);
        return senderFollows || receiverFollows;
    }
}
