package com.belonk.dao;

import com.belonk.entity.Department;
import com.belonk.entity.Employee;
import com.belonk.entity.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface EmployeeDao extends BaseDao<Employee> {
    List<Employee> findByNameLike(String name);

    @Query("select d from Employee e, Department d where e.departmentId = d.id and e.id = :employeeId")
    Department findDepartmentById(@Param("employeeId") Long employeeId);

    List<Employee> findTop3ByNameLikeOrderByIdAsc(String name);

    Employee findTopByNameLikeOrderByIdAsc(String name);

    Employee findFirstByNameLikeOrderByIdAsc(String name);

    List<Employee> findFirst3ByNameLikeOrderByIdAsc(String name);

    Page<Employee> findByNameLikeOrderByIdAsc(String name, Pageable pageable);

    Slice<Employee> findByAgeGreaterThanEqualOrderByIdAsc(int age, Pageable pageable);

    @Query("select e from Employee e where e.name like ?1")
    Stream<Employee> findByCustomQueryAndStream(String name);

    @Async
    Future<List<Employee>> findByGender(Gender gender);
}
