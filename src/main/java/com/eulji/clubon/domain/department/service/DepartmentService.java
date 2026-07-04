package com.eulji.clubon.domain.department.service;

import com.eulji.clubon.domain.department.dto.DepartmentResponse;
import com.eulji.clubon.domain.department.repository.DepartmentRepository;
import com.eulji.clubon.global.error.InvalidDepartmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentResponse> getActiveDepartments() {
        return departmentRepository.findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(DepartmentResponse::from)
                .toList();
    }

    public void validateActiveDepartment(String department) {
        if (!departmentRepository.existsByNameAndActiveTrue(department)) {
            throw new InvalidDepartmentException();
        }
    }
}
