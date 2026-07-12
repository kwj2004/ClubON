package com.eulji.clubon.global.error;

public class DuplicateClubApplicationException extends RuntimeException {

    public DuplicateClubApplicationException() {
        super("이미 처리 대기 중인 가입 신청이 있습니다.");
    }
}
