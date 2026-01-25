package com.florin.franco.UniHub_sistemiWeb.service;

import com.florin.franco.UniHub_sistemiWeb.api.dto.UserCardDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.UserProfileDTO;
import com.florin.franco.UniHub_sistemiWeb.api.mapper.UserMapper;
import com.florin.franco.UniHub_sistemiWeb.dto.UserUpdateRequest;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
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

    @Autowired
    private DipartimentoRepository dipartimentoRepo;

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

    public UserProfileDTO updateUserProfile(Long userId, UserUpdateRequest request) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (isNotBlank(request.getName())) {
            user.setName(request.getName().trim());
        }
        if (isNotBlank(request.getSurname())) {
            user.setSurname(request.getSurname().trim());
        }
        if (isNotBlank(request.getUsername())) {
            String next = request.getUsername().trim();
            if (!next.equals(user.getUsername()) && userRepo.existsByUsername(next)) {
                throw new RuntimeException("Username già esistente");
            }
            user.setUsername(next);
        }
        if (isNotBlank(request.getEmail())) {
            String next = request.getEmail().trim();
            if (!next.equals(user.getEmail()) && userRepo.existsByEmail(next)) {
                throw new RuntimeException("Email già registrata");
            }
            user.setEmail(next);
        }
        if (isNotBlank(request.getStudentId())) {
            String next = request.getStudentId().trim();
            if (!next.equals(user.getStudentId()) && userRepo.existsByStudentId(next)) {
                throw new RuntimeException("Matricola già registrata");
            }
            user.setStudentId(next);
        }
        if (request.getDipartimentoId() != null) {
            Dipartimento dip = dipartimentoRepo.findById(request.getDipartimentoId())
                    .orElseThrow(() -> new RuntimeException("Dipartimento non trovato"));
            user.setDipartimento(dip);
        }

        userRepo.save(user);

        long followerCount = userRepo.countFollower(userId);
        long followingCount = userRepo.countSeguiti(userId);
        List<Evento> recent = eventoRepo.findTop6ByCreatore_IdOrderByDataInizioDesc(userId);

        return UserMapper.toProfileDTO(user, false, followerCount, followingCount, recent);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
