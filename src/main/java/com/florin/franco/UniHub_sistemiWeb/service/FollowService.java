package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;

@Service
public class FollowService {

    @Autowired
    private AppUserRepository userRepo;

    public void segui(Long followerId, Long seguitoId) {
        if (followerId.equals(seguitoId)) throw new RuntimeException("Non puoi seguire te stesso!");

        AppUser follower = userRepo.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower non trovato"));
        AppUser seguito = userRepo.findById(seguitoId)
                .orElseThrow(() -> new RuntimeException("Utente da seguire non trovato"));

        if (follower.getSeguiti().contains(seguito))
            throw new RuntimeException("Stai già seguendo questo utente!");

        follower.getSeguiti().add(seguito);
        userRepo.save(follower);
    }

    public void smettiDiSeguire(Long followerId, Long seguitoId) {
        AppUser follower = userRepo.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower non trovato"));
        AppUser seguito = userRepo.findById(seguitoId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        follower.getSeguiti().remove(seguito);
        userRepo.save(follower);
    }

    public Set<AppUser> getSeguiti(Long userId) {
        return userRepo.findSeguitiByUserId(userId);
    }
}