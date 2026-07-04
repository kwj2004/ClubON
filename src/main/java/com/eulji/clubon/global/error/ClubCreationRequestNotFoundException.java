package com.eulji.clubon.global.error;

public class ClubCreationRequestNotFoundException extends RuntimeException {

    public ClubCreationRequestNotFoundException() {
        super("동아리 개설 신청을 찾을 수 없습니다.");
    }
}
