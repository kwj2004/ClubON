package com.eulji.clubon.global.error;

public class ClubMembershipNotFoundException extends RuntimeException {

    public ClubMembershipNotFoundException() {
        super("동아리 멤버를 찾을 수 없습니다.");
    }
}
