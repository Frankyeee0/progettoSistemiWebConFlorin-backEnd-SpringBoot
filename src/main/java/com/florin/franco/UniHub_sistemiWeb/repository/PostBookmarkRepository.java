package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.PostBookmark;

@Repository
public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
    List<PostBookmark> findByUserId(Long userId);
}
