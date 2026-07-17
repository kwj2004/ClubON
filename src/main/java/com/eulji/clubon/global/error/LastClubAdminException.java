package com.eulji.clubon.global.error;

public class LastClubAdminException extends RuntimeException {
    public LastClubAdminException() {
        super("단독 운영진으로 등록된 동아리가 있어 탈퇴할 수 없습니다. 운영진을 위임한 후 다시 시도해 주세요.");
    }
}
