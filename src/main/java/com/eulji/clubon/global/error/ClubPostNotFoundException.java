package com.eulji.clubon.global.error;

public class ClubPostNotFoundException extends RuntimeException {

    public ClubPostNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }
}
