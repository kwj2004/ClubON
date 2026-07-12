package com.eulji.clubon.global.error;

public class InquiryNotFoundException extends RuntimeException {

    public InquiryNotFoundException() {
        super("문의를 찾을 수 없습니다.");
    }
}
