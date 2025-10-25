package com.florin.franco.UniHub_sistemiWeb.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

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

    Page<AppUser> findByIdNot(Long id, Pageable pageable);

    // esclusione + ricerca su username/nome/cognome
    @Query("""
           SELECT u FROM AppUser u
           WHERE u.id <> :currentId
             AND (
               LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(u.surname) LIKE LOWER(CONCAT('%', :q, '%'))
             )
           """)
    Page<AppUser> searchOthers(@Param("currentId") Long currentId,
                               @Param("q") String q,
                               Pageable pageable);

    @Query("""
           SELECT (COUNT(s)>0) FROM AppUser u
             JOIN u.seguiti s
            WHERE u.id = :followerId AND s.id = :seguitoId
           """)
    boolean existsFollow(@Param("followerId") Long followerId,
                         @Param("seguitoId") Long seguitoId);

    // contatori senza inizializzare le collezioni
    @Query("select count(f) from AppUser u join u.follower f where u.id = :userId")
    long countFollower(@Param("userId") Long userId);

    @Query("select count(s) from AppUser u join u.seguiti s where u.id = :userId")
    long countSeguiti(@Param("userId") Long userId);
}
