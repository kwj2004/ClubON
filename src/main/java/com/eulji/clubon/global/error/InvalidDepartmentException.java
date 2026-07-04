package com.eulji.clubon.global.error;

public class InvalidDepartmentException extends RuntimeException {

    public InvalidDepartmentException() {
        super("등록되지 않은 학과입니다.");
    }
}
