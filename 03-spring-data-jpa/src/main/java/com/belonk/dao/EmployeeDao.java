package com.belonk.dao;

import com.belonk.dao.base.BaseDao;
import com.belonk.pojo.MyEmployee;
import com.belonk.pojo.MyEmployeeDTO;
import com.belonk.pojo.UserConstructWithEmployee;
import com.belonk.pojo.UserConstructWithField;
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

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
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

    @Query("select new com.belonk.pojo.MyEmployeeDTO(e.id, e.name, d.id, d.name) from Employee e, Department d where e.departmentId = d.id and e.id = ?1")
    <T> T findByIdWithDepartment1(Long id, Class<T> clazz);

    @Query("select new com.belonk.pojo.MyEmployeeDTO(e.id, e.name, d.id, d.name) from Employee e, Department d where e.departmentId = d.id and e.id = ?1")
    MyEmployeeDTO findByIdWithDepartment2(Long id);

    @Query("select new com.belonk.pojo.UserConstructWithEmployee(e) from Employee e, Department d where e.departmentId = d.id and e.departmentId = ?1")
    List<UserConstructWithEmployee> findByDeptId1(Long deptId);

    <T> List<T> findByAgeGreaterThan(int age, Class<T> tClass);

    // 试图用接口接收native sql结果，但是不成功，报没有转换器错误 No converter found capable of converting from type
    @Query(value = "select e.id as id, e.name as name, e.age as age, d.id as departmentId, d.name as departmentName " +
            "from employee e, department d where e.departmentId = d.id and e.id = ?1", nativeQuery = true)
    MyEmployee findByIdWithDepartmentUseNativeSqlWithInterfaceBean(Long id);

    // 试图用POJO来接收nativeSQL的结果，还是失败No converter found capable of converting from type
    @Query(value = "select e.id as id, e.name as name, e.age as age, d.id as departmentId, d.name as departmentName " +
            "from employee e, department d where e.departmentId = d.id and e.id = ?1", nativeQuery = true)
    MyEmployeeDTO findByIdWithDepartmentUseNativeSqlWithoutInterfaceBean(Long id);

    // 视图动态投影的方式来处理，仍然失败
    @Query(value = "select e.id as id, e.name as name, e.age as age, d.id as departmentId, d.name as departmentName " +
            "from employee e, department d where e.departmentId = d.id and e.id = ?1", nativeQuery = true)
    <T> T findByIdWithDepartmentUseNativeSqlWithClass(Long id, Class<T> tClass);

    // 仍然转换失败
    @Query(value = "select e.id as id, e.name as name, e.age as age, d.id as departmentId, d.name as departmentName " +
            "from employee e, department d where e.departmentId = d.id and e.id = ?1", nativeQuery = true)
    Map<String, Object> findByIdWithDepartmentUseNativeSqlWithMap(Long id);

    @Query(value = "select e.id as id, e.name as name, e.age as age, d.id as departmentId, d.name as departmentName " +
            "from employee e, department d where e.departmentId = d.id and e.id = ?1", nativeQuery = true)
    List<Tuple> findByIdWithDepartmentUseNativeSqlWithTuple(Long id);

    @Query(value = "select e.id as id, e.name as name, e.age as age from employee e where e.id = ?1", nativeQuery = true)
    MyEmployee findByIdUseNativeSqlWithInterfaceBean(Long id);
}
