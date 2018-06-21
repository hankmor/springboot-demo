package com.belonk.service;

import com.belonk.dao.EmployeeDao;
import com.belonk.domain.User;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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

    private EmployeeDao employeeDao;

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

    public List<User> queryByName(String name) {
        List<Employee> employees = employeeDao.findByNameLike("%" + name + "%");
        List<User> users = new ArrayList<>();
        for (Employee employee : employees) {
            users.add(new User(employee));
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
        Specification<Employee> specification = new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
            }
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

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}