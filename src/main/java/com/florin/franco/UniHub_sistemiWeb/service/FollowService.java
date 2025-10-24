package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class FollowService {
	
	@Autowired
    private  AppUserRepository userRepo;

    @Transactional
    public void segui(Long followerId, Long seguitoId) {
        if (followerId.equals(seguitoId)) {
            throw new RuntimeException("Non puoi seguire te stesso");
        }

        AppUser follower = userRepo.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower non trovato"));
        AppUser seguito = userRepo.findById(seguitoId)
                .orElseThrow(() -> new RuntimeException("Utente da seguire non trovato"));

        if (userRepo.existsFollow(followerId, seguitoId)) return;

        follower.getSeguiti().add(seguito);
        seguito.getFollower().add(follower);

        userRepo.save(follower);
    }

    @Transactional
    public void smettiDiSeguire(Long followerId, Long seguitoId) {
        AppUser follower = userRepo.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower non trovato"));
        AppUser seguito = userRepo.findById(seguitoId)
                .orElseThrow(() -> new RuntimeException("Utente da smettere di seguire non trovato"));

        if (!userRepo.existsFollow(followerId, seguitoId)) return;

        follower.getSeguiti().remove(seguito);
        seguito.getFollower().remove(follower);

        userRepo.save(follower);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long seguitoId) {
        return userRepo.existsFollow(followerId, seguitoId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getFollowInfo(Long userId) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        long followerCount = userRepo.countFollower(userId);
        long seguitiCount  = userRepo.countSeguiti(userId);

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
