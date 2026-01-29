package com.florin.franco.UniHub_sistemiWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.EmailLog;
import com.florin.franco.UniHub_sistemiWeb.repository.EmailLogRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.EmailStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;



@Service
public class EmailService {
	
	 	@Autowired
	    private JavaMailSender mailSender;

        @Autowired
        private EmailLogRepository emailLogRepository;
	
	@Value("${app.mail.from}")
	private String fromEmail;

    @Value("${app.mail.admin:}")
    private String adminEmail;
	 	
	 	
	    public void sendEmail(String to, String subject, String text) {
	        sendEmail(to, subject, text, "GENERIC");
	    }

        public void sendEmail(String to, String subject, String text, String type) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(fromEmail);
            try {
                mailSender.send(message);
                logEmail(to, subject, text, false, type, EmailStatus.SENT, null);
            } catch (RuntimeException e) {
                logEmail(to, subject, text, false, type, EmailStatus.FAILED, e.getMessage());
                throw e;
            }
        }
	    
	    public void sendHtmlEmail(String to, String subject, String htmlContent) {
            sendHtmlEmail(to, subject, htmlContent, "GENERIC");
        }

        public void sendHtmlEmail(String to, String subject, String htmlContent, String type) {
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom(fromEmail);
                helper.setText(htmlContent, true); // true = interpreta HTML

                mailSender.send(message);
                logEmail(to, subject, htmlContent, true, type, EmailStatus.SENT, null);
            } catch (MessagingException e) {
                logEmail(to, subject, htmlContent, true, type, EmailStatus.FAILED, e.getMessage());
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                logEmail(to, subject, htmlContent, true, type, EmailStatus.FAILED, e.getMessage());
                throw e;
            }
	    }
	    
    public void sendWelcomeEmail(String to, String nomeUtente) {
        try {
            String html = loadTemplate("templates/email-welcome.html");
            String today = formatDate(LocalDateTime.now());

            html = html
                    .replace("{{nomeUtente}}", nomeUtente)
                    .replace("{{linkLogin}}", "http://localhost:5173/login")
                    .replace("{{dataInvio}}", today);

            sendHtmlEmail(to, "Benvenuto su UniHub", html, "WELCOME");
            System.out.println("Email di benvenuto inviata a " + to);

	        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email di benvenuto: " + e.getMessage());
        }
    }

    public void sendSupportEmail(String name, String email, String subject, String message) {
        String target = (adminEmail == null || adminEmail.isBlank()) ? fromEmail : adminEmail;
        String safeSubject = (subject == null || subject.isBlank()) ? "Segnalazione UniHub" : subject;
        String text = "Segnalazione da: " + name + " <" + email + ">\n\n" + message;
        sendEmail(target, safeSubject, text, "SUPPORT");
    }

    public void sendNewEventEmail(String to, String creatorUsername, String eventTitle, String when, String where) {
        String subject = "Nuovo evento da " + creatorUsername;
        Map<String, String> details = new LinkedHashMap<>();
        details.put("Titolo", eventTitle);
        details.put("Quando", when);
        details.put("Dove", where);
        sendNotificationEmail(to, subject, "Nuovo evento pubblicato.", details, "EVENT_NEW");
    }

    public void sendEventSignupEmail(String to, String eventTitle, String when, String where) {
        String subject = "Iscrizione confermata";
        Map<String, String> details = new LinkedHashMap<>();
        details.put("Titolo", eventTitle);
        details.put("Quando", when);
        details.put("Dove", where);
        sendNotificationEmail(to, subject, "La tua iscrizione all'evento e' confermata.", details, "EVENT_SIGNUP");
    }

    public void sendEventUpdateEmail(String to, String eventTitle, String when, String where) {
        String subject = "Aggiornamento evento";
        Map<String, String> details = new LinkedHashMap<>();
        details.put("Titolo", eventTitle);
        details.put("Quando", when);
        details.put("Dove", where);
        sendNotificationEmail(to, subject, "Un evento a cui sei iscritto e' stato aggiornato.", details, "EVENT_UPDATE");
    }

    public void sendCommentNotificationEmail(String to, String eventTitle, String commenter) {
        String subject = "Nuovo commento sul tuo evento";
        Map<String, String> details = new LinkedHashMap<>();
        details.put("Evento", eventTitle);
        details.put("Autore", commenter);
        sendNotificationEmail(to, subject, "Hai ricevuto un nuovo commento sul tuo evento.", details, "EVENT_COMMENT");
    }

    private void sendNotificationEmail(String to, String subject, String message, Map<String, String> details, String type) {
        try {
            String html = loadTemplate("templates/email-notification.html");
            String today = formatDate(LocalDateTime.now());
            String detailsHtml = renderDetails(details);

            html = html
                    .replace("{{title}}", subject)
                    .replace("{{message}}", message)
                    .replace("{{dataInvio}}", today)
                    .replace("{{details}}", detailsHtml);

            sendHtmlEmail(to, subject, html, type);
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
        }
    }

    private String loadTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String renderDetails(Map<String, String> details) {
        if (details == null || details.isEmpty()) {
            return "";
        }
        StringBuilder rows = new StringBuilder();
        for (Map.Entry<String, String> entry : details.entrySet()) {
            rows.append("<tr>")
                    .append("<td>").append(entry.getKey()).append("</td>")
                    .append("<td>").append(entry.getValue() == null ? "" : entry.getValue()).append("</td>")
                    .append("</tr>");
        }
        return rows.toString();
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return dateTime.format(formatter);
    }

    private void logEmail(String to, String subject, String body, boolean html, String type, EmailStatus status, String error) {
        try {
            EmailLog log = new EmailLog();
            log.setToEmail(to);
            log.setFromEmail(fromEmail);
            log.setSubject(subject);
            log.setBody(body);
            log.setHtml(html);
            log.setType(type == null || type.isBlank() ? "GENERIC" : type);
            log.setStatus(status);
            log.setErrorMessage(error);
            emailLogRepository.save(log);
        } catch (RuntimeException e) {
            System.err.println("Errore salvataggio log email: " + e.getMessage());
        }
    }
}
