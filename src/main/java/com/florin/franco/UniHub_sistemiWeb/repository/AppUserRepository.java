package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;

@Repository
public interface AppUserRepository  extends JpaRepository
	<AppUser, Long> {
	
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT u.seguiti FROM AppUser u WHERE u.id = :id")
    Set<AppUser> findSeguitiByUserId(@Param("id") Long id);
}
