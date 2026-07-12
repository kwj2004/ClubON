package com.eulji.clubon.global.error;

public class AlreadyClubMemberException extends RuntimeException {

    public AlreadyClubMemberException() {
        super("이미 가입된 동아리입니다.");
    }
}
