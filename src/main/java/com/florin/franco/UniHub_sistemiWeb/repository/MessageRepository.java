package com.florin.franco.UniHub_sistemiWeb.repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Message;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverOrderByCreatedAtDesc(AppUser receiver);
    List<Message> findBySenderOrderByCreatedAtDesc(AppUser sender);
    List<Message> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);

    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.id = :userId AND m.receiver.id = :peerId)
           OR (m.sender.id = :peerId AND m.receiver.id = :userId)
        ORDER BY m.createdAt ASC
        """)
    List<Message> findThread(@Param("userId") Long userId, @Param("peerId") Long peerId);
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.status <> 'READ'")
    Long countUnreadMessages(@Param("userId") Long userId);
}
