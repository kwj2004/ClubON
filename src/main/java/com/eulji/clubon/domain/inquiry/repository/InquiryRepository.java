package com.eulji.clubon.domain.inquiry.repository;

import com.eulji.clubon.domain.inquiry.entity.Inquiry;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;
import com.eulji.clubon.domain.inquiry.entity.InquiryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByMember_EmailOrderByCreatedAtDesc(String email);

    Optional<Inquiry> findByIdAndMember_Email(Long inquiryId, String email);

    @Query("""
        select i
        from Inquiry i
        join fetch i.member
        where (:status is null or i.status = :status)
          and (:type is null or i.type = :type)
        order by i.createdAt desc
        """)
    Page<Inquiry> findAdminInquiries(
        @Param("status") InquiryStatus status,
        @Param("type") InquiryType type,
        Pageable pageable
    );
}
