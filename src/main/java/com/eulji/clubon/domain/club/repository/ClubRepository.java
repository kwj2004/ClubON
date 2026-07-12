package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubCategory;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.eulji.clubon.domain.club.entity.ClubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {

    boolean existsByName(String name);

    long countByType(ClubType type);

    long countByStatus(ClubStatus status);

    @Query("""
            select c
            from Club c
            where (:type is null or c.type = :type)
              and (:category is null or c.category = :category)
              and (:status is null or c.status = :status)
              and (
                    :keyword is null
                    or lower(c.name) like lower(concat('%', :keyword, '%'))
                    or lower(c.shortDescription) like lower(concat('%', :keyword, '%'))
              )
            order by c.createdAt desc, c.id desc
            """)
    List<Club> searchClubs(
            @Param("type") ClubType type,
            @Param("category") ClubCategory category,
            @Param("status") ClubStatus status,
            @Param("keyword") String keyword
    );

    @Query("""
        select c
        from Club c
        where (:type is null or c.type = :type)
          and (:status is null or c.status = :status)
        order by c.createdAt desc, c.id desc
        """)
    List<Club> findAdminClubs(
        @Param("type") ClubType type,
        @Param("status") ClubStatus status
    );
}
