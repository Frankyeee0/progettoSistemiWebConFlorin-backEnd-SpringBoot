package com.florin.franco.UniHub_sistemiWeb.service;

import com.florin.franco.UniHub_sistemiWeb.api.dto.UserCardDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.UserProfileDTO;
import com.florin.franco.UniHub_sistemiWeb.api.mapper.UserMapper;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDirectoryService {

    @Autowired
    private AppUserRepository userRepo;

    @Autowired
    private EventoRepository eventoRepo;

    public Page<UserCardDTO> listUsersExcept(Long currentUserId, String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending().and(Sort.by("surname")));

        Page<AppUser> basePage;
        if (q != null && !q.isBlank()) {
            basePage = userRepo.searchOthers(currentUserId, q.trim(), pageable);
        } else {
            basePage = userRepo.findByIdNot(currentUserId, pageable);
        }

        Set<Long> seguitiIds = userRepo.findSeguitiByUserId(currentUserId)
                .stream().map(AppUser::getId).collect(Collectors.toSet());

        return basePage.map(u -> UserMapper.toCardDTO(u, seguitiIds.contains(u.getId())));
    }

    public UserProfileDTO getUserProfile(Long targetUserId, Long viewerId) {
        AppUser u = userRepo.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        boolean following = viewerId != null && userRepo.existsFollow(viewerId, targetUserId);
        long followerCount = userRepo.countFollower(targetUserId);
        long followingCount = userRepo.countSeguiti(targetUserId);

        List<Evento> recent = eventoRepo.findTop6ByCreatore_IdOrderByDataInizioDesc(targetUserId);

        return UserMapper.toProfileDTO(u, following, followerCount, followingCount, recent);
    }
}
