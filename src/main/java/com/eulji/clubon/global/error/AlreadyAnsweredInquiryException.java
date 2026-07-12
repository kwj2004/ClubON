package com.eulji.clubon.global.error;

public class AlreadyAnsweredInquiryException extends RuntimeException {

    public AlreadyAnsweredInquiryException() {
        super("이미 답변이 완료된 문의입니다.");
    }
}
