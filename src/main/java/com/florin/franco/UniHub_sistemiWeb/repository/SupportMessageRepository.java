package com.florin.franco.UniHub_sistemiWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.SupportMessage;

@Repository
public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {}
