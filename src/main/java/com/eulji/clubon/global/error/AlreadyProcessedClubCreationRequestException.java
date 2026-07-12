package com.eulji.clubon.global.error;

public class AlreadyProcessedClubCreationRequestException extends RuntimeException {

    public AlreadyProcessedClubCreationRequestException() {
        super("이미 처리된 동아리 개설 신청입니다.");
    }
}
