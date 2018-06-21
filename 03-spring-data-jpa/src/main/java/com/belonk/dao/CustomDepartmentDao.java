package com.belonk.dao;

import com.belonk.entity.Department;
import org.springframework.data.repository.Repository;

public interface CustomDepartmentDao extends Repository<Department, Long> {
    Department save(Department department);

    Department findOne(Long id);
}
