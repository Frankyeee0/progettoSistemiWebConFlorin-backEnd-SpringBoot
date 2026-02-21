package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.api.dto.EventListDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.PostDTO;
import com.florin.franco.UniHub_sistemiWeb.service.EventoService;
import com.florin.franco.UniHub_sistemiWeb.service.PostService;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private PostService postService;

    @GetMapping("/events")
    public ResponseEntity<?> savedEvents(@RequestParam Long userId) {
        try {
            List<EventListDTO> list = eventoService.getSavedEvents(userId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/events/{eventId}")
    public ResponseEntity<?> saveEvent(@PathVariable Long eventId, @RequestParam Long userId) {
        try {
            eventoService.addBookmark(eventId, userId);
            return ResponseEntity.ok("Salvato");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> removeEvent(@PathVariable Long eventId, @RequestParam Long userId) {
        try {
            eventoService.removeBookmark(eventId, userId);
            return ResponseEntity.ok("Rimosso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<?> savedPosts(@RequestParam Long userId) {
        try {
            List<PostDTO> list = postService.getSavedPosts(userId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> savePost(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            postService.addBookmark(postId, userId);
            return ResponseEntity.ok("Salvato");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> removePost(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            postService.removeBookmark(postId, userId);
            return ResponseEntity.ok("Rimosso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
