package com.eulji.clubon.global.error;

public class DuplicateClubNameException extends RuntimeException {

    public DuplicateClubNameException() {
        super("이미 같은 이름의 신청 또는 동아리가 존재합니다.");
    }
}
