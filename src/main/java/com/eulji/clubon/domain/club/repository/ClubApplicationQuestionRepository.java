package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubApplicationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubApplicationQuestionRepository
    extends JpaRepository<ClubApplicationQuestion, Long> {

    List<ClubApplicationQuestion>
    findByCreationRequest_IdOrderBySortOrderAsc(Long creationRequestId);

    List<ClubApplicationQuestion>
    findByClub_IdOrderBySortOrderAsc(Long clubId);
}
