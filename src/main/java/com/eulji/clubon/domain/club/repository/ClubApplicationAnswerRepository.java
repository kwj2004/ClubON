package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubApplicationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubApplicationAnswerRepository
    extends JpaRepository<ClubApplicationAnswer, Long> {

    List<ClubApplicationAnswer>
    findByApplication_IdOrderByQuestion_SortOrderAsc(Long applicationId);
}
