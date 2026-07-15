package com.eulji.clubon.global.error;

public class MailSendFailedException extends RuntimeException {

    public MailSendFailedException(Throwable cause) {
        super("인증 메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.", cause);
    }
}
