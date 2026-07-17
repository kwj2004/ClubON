package com.eulji.clubon.global.error;

public class ClubRecruitmentClosedException extends RuntimeException {

    public ClubRecruitmentClosedException() {
        super("현재 모집 중인 동아리가 아니므로 지원할 수 없습니다.");
    }
}
