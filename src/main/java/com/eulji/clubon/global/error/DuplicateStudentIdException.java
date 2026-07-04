package com.eulji.clubon.global.error;

public class DuplicateStudentIdException extends RuntimeException {

    public DuplicateStudentIdException() {
        super("해당 학번으로 이미 가입된 계정이 존재합니다.");
    }
}
