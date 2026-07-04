package com.eulji.clubon.domain.department.controller;

import com.eulji.clubon.domain.department.dto.DepartmentResponse;
import com.eulji.clubon.domain.department.service.DepartmentService;
import com.eulji.clubon.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartments() {
        List<DepartmentResponse> response = departmentService.getActiveDepartments();

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "학과 목록 조회를 성공했습니다.",
                response
        ));
    }
}
