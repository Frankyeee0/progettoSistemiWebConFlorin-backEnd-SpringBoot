package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.florin.franco.UniHub_sistemiWeb.entity.EmailLog;
import com.florin.franco.UniHub_sistemiWeb.utils.EmailStatus;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    List<EmailLog> findTop200ByOrderByCreatedAtDesc();
    List<EmailLog> findTop200ByStatusOrderByCreatedAtDesc(EmailStatus status);
    List<EmailLog> findTop200ByTypeOrderByCreatedAtDesc(String type);
    List<EmailLog> findTop200ByStatusAndTypeOrderByCreatedAtDesc(EmailStatus status, String type);
}
