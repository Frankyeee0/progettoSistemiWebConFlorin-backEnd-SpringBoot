package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.service.FollowService;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private AppUserRepository userRepo;
    @Autowired
    private FollowService followService;

    @PostMapping("/{followerId}/segui/{seguitoId}")
    public ResponseEntity<?> follow(@PathVariable Long followerId, @PathVariable Long seguitoId) {
        try {
            followService.segui(followerId, seguitoId);
            return ResponseEntity.ok("Ora segui l’utente con ID " + seguitoId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{followerId}/unfollow/{seguitoId}")
    public ResponseEntity<?> unFollow(@PathVariable Long followerId, @PathVariable Long seguitoId) {
        try {
            followService.smettiDiSeguire(followerId, seguitoId);
            return ResponseEntity.ok("Hai smesso di seguire l’utente " + seguitoId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity<?> getFollowInfo(@PathVariable Long userId) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return ResponseEntity.ok(Map.of(
            "username", user.getUsername(),
            "followerCount", user.getFollower().size(),
            "seguitiCount", user.getSeguiti().size(),
            "follower", user.getFollower().stream().map(AppUser::getUsername).toList(),
            "seguiti", user.getSeguiti().stream().map(AppUser::getUsername).toList()
        ));
    }
}