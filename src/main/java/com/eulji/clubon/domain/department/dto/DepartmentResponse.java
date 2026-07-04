package com.eulji.clubon.domain.department.dto;

import com.eulji.clubon.domain.department.entity.Department;

public record DepartmentResponse(
        Long id,
        String name
) {

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.getId(), department.getName());
    }
}
