package com.belonk.service;

import com.belonk.dao.CustomEmployeeDao;
import com.belonk.dao.EmployeeDao;
import com.belonk.domain.*;
import com.belonk.entity.Department;
import com.belonk.entity.Employee;
import com.belonk.entity.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Tuple;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sun on 2018/6/21.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
public class EmployeeService extends BaseService<Employee> {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(EmployeeService.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private CustomEmployeeDao customEmployeeDao;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    public EmployeeService(EmployeeDao employeeDao) {
        super(employeeDao);
        this.employeeDao = employeeDao;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    // query

    public List<UserConstructWithField> queryByName(String name) {
        List<Employee> employees = employeeDao.findByNameLike("%" + name + "%");
        List<UserConstructWithField> users = new ArrayList<>();
        for (Employee employee : employees) {
//            users.add(new UserConstructWithField(employee));
        }
        return users;
    }

    public Department queryDepartmentOfEmployee(Long employeeId) {
        return employeeDao.findDepartmentById(employeeId);
    }

    // top and first query

    public List<Employee> queryTop3ByName(String name) {
        return employeeDao.findTop3ByNameLikeOrderByIdAsc("%" + name + "%");
    }

    public Employee queryTopByName(String name) {
        return employeeDao.findTopByNameLikeOrderByIdAsc("%" + name + "%");
    }

    public Employee queryFirstByName(String name) {
        return employeeDao.findFirstByNameLikeOrderByIdAsc("%" + name + "%");
    }

    public List<Employee> queryFirst3ByName(String name) {
        return employeeDao.findFirst3ByNameLikeOrderByIdAsc("%" + name + "%");
    }

    // page query

    public Page<Employee> pageQueryByName(int pageIndex, int pageSize, String name) {
        // pageIndex从0开始
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        return employeeDao.findByNameLikeOrderByIdAsc(name, pageable);
    }

    public Slice<Employee> pageQueryByAge(int pageIndex, int pageSize, int minAge) {
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        return employeeDao.findByAgeGreaterThanEqualOrderByIdAsc(minAge, pageable);
    }

    public Page<Employee> pageQueryByNameAndAage(int pageIndex, int pageSize, String name, int minAge) {
        // 创建查询条件规则
        Specification<Employee> specification = (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasLength(name)) {
                predicates.add(cb.and(cb.like(root.get("name"), name)));
            }
            if (minAge > 0) {
                predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("age"), minAge)));
            }
            if (predicates.size() > 0) {
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
            }
            return cq.getRestriction();
        };
        // 创建排序规则
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        // 创建分页对象
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return employeeDao.findAll(specification, pageable);
    }

    // stream results

    @Transactional
    public List<Employee> queryByNameFilterWithAge(String name, int minAge) {
        try (Stream<Employee> employeeStream = employeeDao.findByCustomQueryAndStream(name);) {
            return employeeStream.filter(employee -> employee.getAge() >= minAge).collect(Collectors.toList());
        }
    }

    // async query

    public List<Employee> queryByGenderAsync(Gender gender) {
        Future<List<Employee>> employeeFuture = employeeDao.findByGender(gender);
        log.info("querying ... ");
        try {
            log.info("do something else ...");
            return employeeFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    // named query

    public List<Employee> queryByDeptId(Long deptId) {
        return employeeDao.findByDeptId(deptId);
    }

    public List<Employee> queryByGender(Gender gender) {
        return employeeDao.findByGender1(gender);
    }

    // modifying query

    public int reverseGenderOfFemale() {
        return employeeDao.reverseGenderOfFemale();
    }

    public void deleteByDeptId(Long deptId) {
        employeeDao.deleteInBulkByDeptId(deptId);
    }

    // query projections

    public UserConstructWithField queryById(Long id) {
        return employeeDao.findById(id);
    }

    public List<UserConstructWithField> queryByName1(String name) {
        return employeeDao.findByNameIsLike("%" + name + "%");
    }

    public MyEmployee queryByIdWithDepartment(Long id) {
        return employeeDao.findByIdWithDepartment(id);
    }

    public MyEmployeeDTO queryByIdWithDepartmentUseNativeSql(Long id) {
        return customEmployeeDao.findByIdWithDepartment(id);
    }

    public MyEmployeeDTO queryByIdWithDepartmentUseJPQL(Long id) {
        return employeeDao.findByIdWithDepartment2(id);
    }

    public MyEmployeeDTO queryByIdWithDepartmentUseJPQL1(Long id) {
        return employeeDao.findByIdWithDepartment1(id, MyEmployeeDTO.class);
    }

    public List<UserConstructWithEmployee> queryByDeptId1(Long deptId) {
        return employeeDao.findByDeptId1(deptId);
    }

    public List<UserConstructWithField> queryByAgeGreaterThan(int minAage) {
        return employeeDao.findByAgeGreaterThan(minAage, UserConstructWithField.class);
    }

    public List<UserConstructWithField1> queryByAgeGreaterThan1(int minAage) {
        return employeeDao.findByAgeGreaterThan(minAage, UserConstructWithField1.class);
    }

    public MyEmployee queryByIdWithDepartmentUseNativeSqlWithInterfaceBean(Long id) {
        // 视图用接口来接收nativeSQL的结果，不成功，抛出org.springframework.core.convert.ConverterNotFoundException: No converter
        // found capable of converting from type [java.math.BigInteger] to type [com.belonk.domain.MyEmployee]
        return employeeDao.findByIdWithDepartmentUseNativeSqlWithInterfaceBean(id);
    }

    public MyEmployeeDTO queryByIdWithDepartmentUseNativeSqlWithoutInterfaceBean(Long id) {
        // 视图用接口来接收nativeSQL的结果，仍然不成功，抛出org.springframework.core.convert.ConverterNotFoundException: No converter
        // found capable of converting from type [java.math.BigInteger] to type [com.belonk.domain.MyEmployee]
        return employeeDao.findByIdWithDepartmentUseNativeSqlWithoutInterfaceBean(id);
    }

    public MyEmployeeDTO queryByIdWithDepartmentUseNativeSqlWithClass(Long id) {
        // 视图用接口来接收nativeSQL的结果，仍然不成功，抛出org.springframework.core.convert.ConverterNotFoundException: No converter
        // found capable of converting from type [java.math.BigInteger] to type [com.belonk.domain.MyEmployee]
        return employeeDao.findByIdWithDepartmentUseNativeSqlWithClass(id, MyEmployeeDTO.class);
    }

    public MyEmployeeDTO queryByIdWithDepartmentUseNativeSqlWithMap(Long id) {
        // 视图用接口来接收nativeSQL的结果，仍然不成功，抛出org.springframework.core.convert.ConversionFailedException
        Map map = employeeDao.findByIdWithDepartmentUseNativeSqlWithMap(id);
        System.out.println(map);
        return null;
    }

    public MyEmployeeDTO queryByIdWithDepartmentUseNativeSqlWithTuple(Long id) {
        // 视图用接口来接收nativeSQL的结果，仍然不成功，抛出org.springframework.core.convert.ConversionFailedException
        List<Tuple> tuples = employeeDao.findByIdWithDepartmentUseNativeSqlWithTuple(id);
        System.out.println(tuples);
        return null;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}