package com.florin.franco.UniHub_sistemiWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.SupportRequest;
import com.florin.franco.UniHub_sistemiWeb.entity.SupportMessage;
import com.florin.franco.UniHub_sistemiWeb.repository.SupportMessageRepository;

@Service
public class SupportService {

    @Autowired
    private SupportMessageRepository supportMessageRepository;

    @Autowired
    private EmailService emailService;

    public void handleSupportRequest(SupportRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Nome mancante");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email mancante");
        }
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            throw new RuntimeException("Messaggio mancante");
        }

        SupportMessage msg = new SupportMessage();
        msg.setName(request.getName());
        msg.setEmail(request.getEmail());
        msg.setSubject(request.getSubject());
        msg.setMessage(request.getMessage());
        supportMessageRepository.save(msg);

        emailService.sendSupportEmail(
                request.getName(),
                request.getEmail(),
                request.getSubject(),
                request.getMessage()
        );
    }
}
