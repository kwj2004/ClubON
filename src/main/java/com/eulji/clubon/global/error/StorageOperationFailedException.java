package com.eulji.clubon.global.error;

public class StorageOperationFailedException extends RuntimeException {
    public StorageOperationFailedException(Throwable cause) {
        super("파일 저장소 처리에 실패했습니다. 잠시 후 다시 시도해주세요.", cause);
    }
}
