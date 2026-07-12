package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubPost;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

    @Query(
        value = """
                select p
                from ClubPost p
                join fetch p.author
                where p.club.id = :clubId
                  and p.status = com.eulji.clubon.domain.club.entity.ClubPostStatus.PUBLISHED
                  and (:category is null or p.category = :category)
                  and (
                        :keyword is null
                        or lower(p.title) like lower(concat('%', :keyword, '%'))
                        or lower(p.content) like lower(concat('%', :keyword, '%'))
                      )
                order by p.createdAt desc
                """,
        countQuery = """
                select count(p)
                from ClubPost p
                where p.club.id = :clubId
                  and p.status = com.eulji.clubon.domain.club.entity.ClubPostStatus.PUBLISHED
                  and (:category is null or p.category = :category)
                  and (
                        :keyword is null
                        or lower(p.title) like lower(concat('%', :keyword, '%'))
                        or lower(p.content) like lower(concat('%', :keyword, '%'))
                      )
                """
    )
    Page<ClubPost> findPublishedPosts(
        @Param("clubId") Long clubId,
        @Param("category") ClubPostCategory category,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    @Query("""
            select p
            from ClubPost p
            join fetch p.club
            join fetch p.author
            where p.id = :postId
              and p.club.id = :clubId
              and p.status = com.eulji.clubon.domain.club.entity.ClubPostStatus.PUBLISHED
            """)
    Optional<ClubPost> findPublishedPostDetail(
        @Param("clubId") Long clubId,
        @Param("postId") Long postId
    );

    @Query("""
            select p
            from ClubPost p
            join fetch p.club
            join fetch p.author
            where p.id = :postId
              and p.club.id = :clubId
            """)
    Optional<ClubPost> findByClubIdAndPostId(
        @Param("clubId") Long clubId,
        @Param("postId") Long postId
    );

    @Query(
        value = """
                select p
                from ClubPost p
                join fetch p.author
                where p.club.id = :clubId
                  and p.author.email = :email
                  and p.status = com.eulji.clubon.domain.club.entity.ClubPostStatus.DRAFT
                order by p.updatedAt desc
                """,
        countQuery = """
                select count(p)
                from ClubPost p
                where p.club.id = :clubId
                  and p.author.email = :email
                  and p.status = com.eulji.clubon.domain.club.entity.ClubPostStatus.DRAFT
                """
    )
    Page<ClubPost> findMyDraftPosts(
        @Param("clubId") Long clubId,
        @Param("email") String email,
        Pageable pageable
    );

    @Query(
        value = """
                select p
                from ClubPost p
                join fetch p.club
                join fetch p.author
                where p.author.email = :email
                  and (:category is null or p.category = :category)
                order by p.createdAt desc
                """,
        countQuery = """
                select count(p)
                from ClubPost p
                where p.author.email = :email
                  and (:category is null or p.category = :category)
                """
    )
    Page<ClubPost> findMyPosts(
        @Param("email") String email,
        @Param("category") ClubPostCategory category,
        Pageable pageable
    );

    long countByAuthor_Email(String email);

    long countByClub_Id(Long clubId);
}
