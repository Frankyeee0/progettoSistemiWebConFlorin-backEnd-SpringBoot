package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.api.dto.UserCardDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.UserProfileDTO;
import com.florin.franco.UniHub_sistemiWeb.service.UserDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserDirectoryController {

    @Autowired
    private UserDirectoryService service;

    @GetMapping
    public Page<UserCardDTO> listUsers(
            @RequestParam Long currentUserId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return service.listUsersExcept(currentUserId, q, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id,
                                        @RequestParam(required = false) Long viewerId) {
        try {
            UserProfileDTO dto = service.getUserProfile(id, viewerId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
