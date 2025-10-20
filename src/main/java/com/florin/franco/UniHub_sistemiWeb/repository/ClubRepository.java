package com.florin.franco.UniHub_sistemiWeb.repository;

import com.florin.franco.UniHub_sistemiWeb.dto.ClubDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Club;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<ClubDto> findAllProjectedBy();
    Optional<ClubDto> findProjectedById(Long id);
}