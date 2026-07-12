package com.eulji.clubon.global.error;

public class ClubApplicationNotFoundException extends RuntimeException {

    public ClubApplicationNotFoundException() {
        super("가입 신청서를 찾을 수 없습니다.");
    }
}
