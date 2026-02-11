package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.api.dto.PostCommentDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.PostCommentRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.PostCreateRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.PostDTO;
import com.florin.franco.UniHub_sistemiWeb.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/feed")
    public ResponseEntity<List<PostDTO>> feed(@RequestParam Long userId) {
        return ResponseEntity.ok(postService.getFeed(userId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long userId, @RequestBody PostCreateRequest request) {
        try {
            return ResponseEntity.ok(postService.createPost(userId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> like(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            return ResponseEntity.ok(postService.likePost(postId, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            return ResponseEntity.ok(postService.unlikePost(postId, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody PostCommentRequest request
    ) {
        try {
            PostCommentDTO dto = postService.addComment(postId, userId, request);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam Long userId
    ) {
        try {
            postService.deleteComment(postId, commentId, userId);
            return ResponseEntity.ok("Commento eliminato");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
