package com.belonk.service;

import com.belonk.dao.DefaultEmployeeDao;
import com.belonk.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by sun on 2018/6/27.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
public class DefaultEmployeeService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(DefaultEmployeeService.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private DefaultEmployeeDao employeeDao;
    @Autowired
    private EntityManager entityManager;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    // CRUD

    public Employee add(Employee employee) {
        Assert.isNull(employee.getId(), "Id must be null.");
        return employeeDao.save(employee);
    }

    public Employee update(Employee employee) {
        Assert.notNull(employee.getId(), "Id must noe be null.");
        return employeeDao.save(employee);
    }

    public void delete(Long id) {
        Assert.notNull(id, "Id must not be null.");
        employeeDao.delete(id);
    }

    public List<Employee> queryAll() {
        return employeeDao.findAll();
    }

    public String getName(Long id) {
        Employee employee = employeeDao.getOne(id);
        return employee.getName();
    }

    public Employee getById(Long id) {
        return employeeDao.findOne(id);
    }

    public List<Employee> queryByName(String name) {
        Assert.hasLength(name, "Name must be null.");
        name = "%" + name + "%";
        return employeeDao.findByNameLike(name);
    }

    // Transaction

    @Transactional
    public Employee create(Employee employee) throws Exception {
        employee = employeeDao.save(employee);
        // 未指定rollback的异常，不会回滚
        throw new Exception("throw exception");
        // return employee;
    }

    @Transactional(rollbackFor = Exception.class)
    public Employee createRollback(Employee employee) throws Exception {
        employee = employeeDao.save(employee);
        // 抛出Exception，事务会回滚
        throw new Exception("throw exception");
        // return employee;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Employee createRollback1(Employee employee) throws Exception {
        employee = employeeDao.save(employee);
        // 抛出Exception，事务不会回滚
        throw new Exception("throw Exception");
        // return employee;
    }

    @Transactional(rollbackFor = Exception.class)
    public Employee createRollback2(Employee employee) throws Exception {
        employee = employeeDao.save(employee);
        // 抛出RuntimeException，事务会回滚
        throw new RuntimeException("throw RuntimeException");
        // return employee;
    }

    @Transactional(rollbackFor = Exception.class)
    public Employee updateAfterGet(Employee employee) {
        employee = employeeDao.getOne(employee.getId());
        System.out.println(entityManager.contains(employee));
        employee.setAge(100);
        try {
            subMethod();
        } catch (Exception e) {
            // 调用了setAge方法，age会直接更新，不论是否调用save
            return employee;
        }
        employeeDao.save(employee);
        return employee;
    }

    @Transactional(rollbackFor = Exception.class)
    public Employee updateAfterGet1(Employee employee) {
        employee = employeeDao.getOne(employee.getId());
        System.out.println(entityManager.contains(employee));
        employee.setAge(100);
        try {
            subMethod();
        } catch (Exception e) {
            // 调用entityManager.clear，取消更新
            entityManager.clear();
            return employee;
        }
        employeeDao.save(employee);
        return employee;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private void subMethod() {
        throw new RuntimeException("throw exception");
    }
}