package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;

public interface AppUserRepository  extends JpaRepository
	<AppUser, Long> {
	Optional<AppUser> findByUsername(String username);

}
