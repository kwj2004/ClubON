package com.eulji.clubon.domain.department.repository;

import com.eulji.clubon.domain.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByNameAndActiveTrue(String name);

    List<Department> findAllByActiveTrueOrderByNameAsc();
}
