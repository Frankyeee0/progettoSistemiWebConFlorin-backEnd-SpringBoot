package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.florin.franco.UniHub_sistemiWeb.entity.EmailHistory;
import com.florin.franco.UniHub_sistemiWeb.utils.EmailStatus;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {
    List<EmailHistory> findTop200ByOrderByCreatedAtDesc();
    List<EmailHistory> findTop200ByStatusOrderByCreatedAtDesc(EmailStatus status);
    List<EmailHistory> findTop200ByTypeOrderByCreatedAtDesc(String type);
    List<EmailHistory> findTop200ByStatusAndTypeOrderByCreatedAtDesc(EmailStatus status, String type);
}
