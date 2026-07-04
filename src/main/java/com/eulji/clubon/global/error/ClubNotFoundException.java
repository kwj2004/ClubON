package com.eulji.clubon.global.error;

public class ClubNotFoundException extends RuntimeException {

    public ClubNotFoundException() {
        super("동아리를 찾을 수 없습니다.");
    }
}
