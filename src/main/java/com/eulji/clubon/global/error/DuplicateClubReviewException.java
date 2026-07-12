package com.eulji.clubon.global.error;

public class DuplicateClubReviewException extends RuntimeException {

    public DuplicateClubReviewException() {
        super("이미 해당 동아리에 작성한 후기가 있습니다.");
    }
}
