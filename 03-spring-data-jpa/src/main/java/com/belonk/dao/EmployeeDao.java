package com.belonk.dao;

import com.belonk.dao.base.BaseDao;
import com.belonk.domain.MyEmployee;
import com.belonk.domain.MyEmployeeDTO;
import com.belonk.domain.UserConstructWithEmployee;
import com.belonk.domain.UserConstructWithField;
import com.belonk.entity.Department;
import com.belonk.entity.Employee;
import com.belonk.entity.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
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

    // modifying query

    @Modifying
    @Query("update Employee e set e.gender = com.belonk.entity.Gender.MALE where e.gender = com.belonk.entity.Gender.FEMALE")
    int reverseGenderOfFemale();

    @Modifying
    @Query("delete from Employee e where e.departmentId = ?1")
    void deleteInBulkByDeptId(Long deptId);

    // named query

    List<Employee> findByDeptId(Long deptId);

    List<Employee> findByGender1(Gender gender);

    // projections

    UserConstructWithField findById(Long id);

    List<UserConstructWithField> findByNameIsLike(String name);

    @Query("select e.id as id, e.name as name, e.age as age, d.id as departmentId, d.name as departmentName from Employee e, Department d where e.departmentId = d.id and e.id = ?1")
    MyEmployee findByIdWithDepartment(Long id);

    @Query("select new com.belonk.domain.MyEmployeeDTO(e.id, e.name, d.id, d.name) from Employee e, Department d where e.departmentId = d.id and e.id = ?1")
    <T> T findByIdWithDepartment1(Long id, Class<T> clazz);

    @Query("select new com.belonk.domain.MyEmployeeDTO(e.id, e.name, d.id, d.name) from Employee e, Department d where e.departmentId = d.id and e.id = ?1")
    MyEmployeeDTO findByIdWithDepartment2(Long id);

    @Query("select new com.belonk.domain.UserConstructWithEmployee(e) from Employee e, Department d where e.departmentId = d.id and e.departmentId = ?1")
    List<UserConstructWithEmployee> findByDeptId1(Long deptId);

    <T> List<T> findByAgeGreaterThan(int age, Class<T> tClass);
}
