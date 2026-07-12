package com.eulji.clubon.global.error;

public class ClubReviewNotFoundException extends RuntimeException {

    public ClubReviewNotFoundException() {
        super("후기를 찾을 수 없습니다.");
    }
}
