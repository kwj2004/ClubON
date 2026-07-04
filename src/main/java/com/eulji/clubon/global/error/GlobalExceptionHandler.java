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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(
                        HttpStatus.FORBIDDEN.value(),
                        "ACCESS_DENIED",
                        e.getMessage()
                ));
    }
}
