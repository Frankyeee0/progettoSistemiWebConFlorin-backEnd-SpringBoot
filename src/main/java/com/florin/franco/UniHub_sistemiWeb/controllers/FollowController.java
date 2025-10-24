package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.service.FollowService;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@CrossOrigin // se serve
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{currentUserId}/segui/{toFolloweUserId}")
    public ResponseEntity<?> segui(@PathVariable Long currentUserId, @PathVariable Long toFolloweUserId) {
        try {
            followService.segui(currentUserId, toFolloweUserId);
            return ResponseEntity.ok(Map.of(
                    "message", "Ora segui l’utente",
                    "followerId", currentUserId,
                    "seguitoId", toFolloweUserId
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{followerId}/unfollow/{seguitoId}")
    public ResponseEntity<?> smetti(@PathVariable Long followerId, @PathVariable Long seguitoId) {
        try {
            followService.smettiDiSeguire(followerId, seguitoId);
            return ResponseEntity.ok(Map.of(
                    "message", "Hai smesso di seguire l’utente",
                    "followerId", followerId,
                    "seguitoId", seguitoId
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 👇 Stato "io seguo X?"
    @GetMapping("/{followerId}/status/{seguitoId}")
    public ResponseEntity<?> isFollowing(@PathVariable Long followerId, @PathVariable Long seguitoId) {
        try {
            boolean following = followService.isFollowing(followerId, seguitoId);
            return ResponseEntity.ok(following);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // Info follow (contatori + liste username)
    @GetMapping("/{userId}/info")
    public ResponseEntity<?> getFollowInfo(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(followService.getFollowInfo(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


}
