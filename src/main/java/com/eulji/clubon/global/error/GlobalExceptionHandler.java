package com.eulji.clubon.global.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StorageOperationFailedException.class)
    public ResponseEntity<ErrorResponse> handleStorageOperationFailedException(StorageOperationFailedException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.of(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "STORAGE_OPERATION_FAILED",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(MailSendFailedException.class)
    public ResponseEntity<ErrorResponse> handleMailSendFailedException(MailSendFailedException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.of(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "MAIL_SEND_FAILED",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldErrorResponse> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();
        String message = errors.isEmpty() ? "입력 값이 올바르지 않습니다." : errors.get(0).reason();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.withErrors(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_INPUT_VALUE",
                        message,
                        errors
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<FieldErrorResponse> errors = e.getConstraintViolations()
                .stream()
                .map(error -> new FieldErrorResponse(error.getPropertyPath().toString(), error.getMessage()))
                .toList();
        String message = errors.isEmpty() ? "입력 값이 올바르지 않습니다." : errors.get(0).reason();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.withErrors(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_INPUT_VALUE",
                        message,
                        errors
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_INPUT_VALUE",
                        "모집 상태는 OPEN 또는 CLOSED만 가능합니다."
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_INPUT_VALUE",
                        "요청 파라미터 값이 올바르지 않습니다."
                ));
    }

    @ExceptionHandler(InvalidClubUpdateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidClubUpdateException(InvalidClubUpdateException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_INPUT_VALUE",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                        HttpStatus.CONFLICT.value(),
                        "DUPLICATE_EMAIL",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateStudentIdException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateStudentIdException(DuplicateStudentIdException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                        HttpStatus.CONFLICT.value(),
                        "DUPLICATE_STUDENT_ID",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateClubNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateClubNameException(DuplicateClubNameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                        HttpStatus.CONFLICT.value(),
                        "DUPLICATE_CLUB_NAME",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateClubApplicationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateClubApplicationException(DuplicateClubApplicationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "DUPLICATE_CLUB_APPLICATION",
                e.getMessage()
            ));
    }

    @ExceptionHandler(AlreadyClubMemberException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyClubMemberException(AlreadyClubMemberException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "ALREADY_CLUB_MEMBER",
                e.getMessage()
            ));
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailedException(LoginFailedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(
                        HttpStatus.UNAUTHORIZED.value(),
                        "LOGIN_FAILED",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(InvalidDepartmentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDepartmentException(InvalidDepartmentException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_INPUT_VALUE",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubNotFoundException(ClubNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "CLUB_NOT_FOUND",
                        e.getMessage()
                ));
    }
    @ExceptionHandler(ClubRecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubRecordNotFoundException(ClubRecordNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "CLUB_RECORD_NOT_FOUND",
                e.getMessage()
            ));
    }

    @ExceptionHandler(ClubPostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubPostNotFoundException(ClubPostNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "CLUB_POST_NOT_FOUND",
                e.getMessage()
            ));
    }

    @ExceptionHandler(ClubReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubReviewNotFoundException(ClubReviewNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "CLUB_REVIEW_NOT_FOUND",
                e.getMessage()
            ));
    }

    @ExceptionHandler(DuplicateClubReviewException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateClubReviewException(DuplicateClubReviewException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "DUPLICATE_CLUB_REVIEW",
                e.getMessage()
            ));
    }

    @ExceptionHandler(ClubMembershipNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubMembershipNotFoundException(ClubMembershipNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "CLUB_MEMBERSHIP_NOT_FOUND",
                e.getMessage()
            ));
    }

    @ExceptionHandler(ClubApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubApplicationNotFoundException(ClubApplicationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "CLUB_APPLICATION_NOT_FOUND",
                e.getMessage()
            ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_APPLICATION_STATUS",
                e.getMessage()
            ));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "MEMBER_NOT_FOUND",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(ClubCreationRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubCreationRequestNotFoundException(ClubCreationRequestNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "CLUB_CREATION_REQUEST_NOT_FOUND",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(InquiryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInquiryNotFoundException(InquiryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "INQUIRY_NOT_FOUND",
                e.getMessage()
            ));
    }

    @ExceptionHandler(AlreadyAnsweredInquiryException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyAnsweredInquiryException(
        AlreadyAnsweredInquiryException e
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "INQUIRY_ALREADY_ANSWERED",
                e.getMessage()
            ));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotificationNotFoundException(NotificationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "NOTIFICATION_NOT_FOUND",
                e.getMessage()
            ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(
                        HttpStatus.FORBIDDEN.value(),
                        "ACCESS_DENIED",
                        e.getMessage()
                ));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_INPUT_VALUE",
                e.getMessage()
            ));
    }
    @ExceptionHandler(AlreadyProcessedClubCreationRequestException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyProcessedClubCreationRequestException(
        AlreadyProcessedClubCreationRequestException e
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "ALREADY_PROCESSED_CLUB_CREATION_REQUEST",
                e.getMessage()
            ));
    }
}
