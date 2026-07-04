package com.eulji.clubon.global.error;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super("이미 가입되어 있는 이메일 주소입니다.");
    }
}
