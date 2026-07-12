package com.eulji.clubon.global.error;

public class ClubRecordNotFoundException extends RuntimeException {

    public ClubRecordNotFoundException() {
        super("활동 기록을 찾을 수 없습니다.");
    }
}
