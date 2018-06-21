package com.belonk.dao;

import com.belonk.entity.Department;
import com.belonk.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeDao extends BaseDao<Employee> {
    List<Employee> findByNameLike(String name);

    @Query("select d from Employee e, Department d where e.departmentId = d.id and e.id = :employeeId")
    Department findDepartmentById(@Param("employeeId") Long employeeId);
}
