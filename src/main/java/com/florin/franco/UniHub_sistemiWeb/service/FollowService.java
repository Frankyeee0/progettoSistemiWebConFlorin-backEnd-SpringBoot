package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final AppUserRepository userRepo;

<<<<<<< HEAD
=======
    @Transactional
>>>>>>> release
    public void segui(Long followerId, Long seguitoId) {
        if (followerId.equals(seguitoId)) {
            throw new RuntimeException("Non puoi seguire te stesso");
        }

        AppUser follower = userRepo.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower non trovato"));
        AppUser seguito = userRepo.findById(seguitoId)
                .orElseThrow(() -> new RuntimeException("Utente da seguire non trovato"));

        // ✅ idempotenza — se già lo segue, non fare nulla
        if (userRepo.existsFollow(followerId, seguitoId)) return;

        follower.getSeguiti().add(seguito);
        // mantenere coerente anche lato inverso (solo in memoria)
        seguito.getFollower().add(follower);

        userRepo.save(follower);
    }

<<<<<<< HEAD
=======
    @Transactional
>>>>>>> release
    public void smettiDiSeguire(Long followerId, Long seguitoId) {
        AppUser follower = userRepo.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower non trovato"));
        AppUser seguito = userRepo.findById(seguitoId)
                .orElseThrow(() -> new RuntimeException("Utente da smettere di seguire non trovato"));

        // ✅ se non lo segue già, non serve fare nulla
        if (!userRepo.existsFollow(followerId, seguitoId)) return;

        follower.getSeguiti().remove(seguito);
        seguito.getFollower().remove(follower);

        userRepo.save(follower);
    }

<<<<<<< HEAD
    public Set<AppUser> getSeguiti(Long userId) {
        return userRepo.findSeguitiByUserId(userId);
=======
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long seguitoId) {
        // ✅ controlla via query, senza caricare entità pesanti
        return userRepo.existsFollow(followerId, seguitoId);
>>>>>>> release
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getFollowInfo(Long userId) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // ✅ query di conteggio più leggere (evita LazyInitialization)
        long followerCount = userRepo.countFollower(userId);
        long seguitiCount  = userRepo.countSeguiti(userId);

        // ✅ opzionale: lista username per visualizzazioni leggere
        List<String> follower = user.getFollower().stream()
                .map(AppUser::getUsername)
                .toList();
        List<String> seguiti = user.getSeguiti().stream()
                .map(AppUser::getUsername)
                .toList();

        return Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "followerCount", followerCount,
                "seguitiCount", seguitiCount,
                "follower", follower,
                "seguiti", seguiti
        );
    }
}
