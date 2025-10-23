package com.florin.franco.UniHub_sistemiWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;



@Service
public class EmailService {
	
	 	@Autowired
	    private JavaMailSender mailSender;
	
	 	@Value("${app.mail.from}")
	    private String fromEmail;
	 	
	 	
	    public void sendEmail(String to, String subject, String text) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(text);
	        message.setFrom(fromEmail); 
	        mailSender.send(message);
	    }
	    
	    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setFrom(fromEmail);
	        helper.setText(htmlContent, true); // true = interpreta HTML

	        mailSender.send(message);
	    }
	    
	    public void sendWelcomeEmail(String to, String nomeUtente) {
	        try {
	        	ClassPathResource resource = new ClassPathResource("templates/email-template.html");
	        	
	            try (InputStream inputStream = resource.getInputStream()) {
	                String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

	                html = html
	                        .replace("{{nomeUtente}}", nomeUtente)
	                        .replace("{{linkLogin}}", "https://unihub.it/login");

	                sendHtmlEmail(to, "Benvenuto su UniHub 🎓", html);
	                System.out.println("Email di benvenuto inviata a " + to);
	            }

	        } catch (Exception e) {
	            System.err.println("Errore durante l'invio dell'email di benvenuto: " + e.getMessage());
	        }
	    }
}
