package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubBookmarkRepository extends JpaRepository<ClubBookmark, Long> {

    Optional<ClubBookmark> findByClub_IdAndMember_Email(Long clubId, String email);

    boolean existsByClub_IdAndMember_Email(Long clubId, String email);

    @Query("""
            select cb
            from ClubBookmark cb
            join fetch cb.club
            join cb.member m
            where m.email = :email
            order by cb.createdAt desc, cb.id desc
            """)
    List<ClubBookmark> findMyBookmarks(@Param("email") String email);
}
