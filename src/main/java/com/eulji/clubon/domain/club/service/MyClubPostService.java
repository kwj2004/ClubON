package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.MyClubPostListResponse;
import com.eulji.clubon.domain.club.dto.MyClubPostPageResponse;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import com.eulji.clubon.domain.club.repository.ClubPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyClubPostService {

    private final ClubPostRepository clubPostRepository;

    public MyClubPostPageResponse getMyPosts(
        String email,
        ClubPostCategory category,
        int page,
        int size
    ) {
        validatePageRequest(page, size);

        return MyClubPostPageResponse.from(
            clubPostRepository.findMyPosts(email, category, PageRequest.of(page, size))
                .map(MyClubPostListResponse::from)
        );
    }

    public long countMyPosts(String email) {
        return clubPostRepository.countByAuthor_Email(email);
    }

    private void validatePageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("페이지 크기는 1 이상 100 이하로 입력해주세요.");
        }
    }
}
