package com.belonk.service;

import com.belonk.BaseTest;
import com.belonk.entity.Employee;
import com.belonk.entity.Gender;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

/**
 * Created by sun on 2018/6/27.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class DefaultEmployeeServiceTest extends BaseTest {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(DefaultEmployeeServiceTest.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private DefaultEmployeeService employeeService;

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

    @Test
    public void testFindByName() {
        String name = "a";
        List<Employee> employees = employeeService.queryByName(name);
        System.out.println("employees : " + employees);
        Assert.assertTrue(employees.size() == 2);
    }

    @Test
    public void testGetById() {
        Long id = 10000L;
        String name = employeeService.getName(id);
        System.out.println(name);
        Employee employee1 = employeeService.getById(id);
        Assert.assertNull(employee1);
    }

    @Test
    public void testCreate() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test");
        try {
            // 不会回滚
            employeeService.create(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = employee.getId();
        System.out.println("id : " + id);
        employee = employeeService.getById(id);
        Assert.assertNotNull(employee);
    }

    @Test
    public void testCreateRollback() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test");
        try {
            // 回滚
            employeeService.createRollback(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = employee.getId();
        System.out.println("id : " + id);
        employee = employeeService.getById(id);
        Assert.assertNull(employee);
    }

    @Test
    public void testCreateRollback1() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test");
        try {
            // 不会回滚
            employeeService.createRollback1(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = employee.getId();
        System.out.println("id : " + id);
        employee = employeeService.getById(id);
        Assert.assertNotNull(employee);
    }

    @Test
    public void testCreateRollback2() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test");
        try {
            // 会回滚
            employeeService.createRollback2(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = employee.getId();
        System.out.println("id : " + id);
        employee = employeeService.getById(id);
        Assert.assertNull(employee);
    }

    @Test
    public void testUpdate() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test");
        try {
            // 不会回滚
            employeeService.create(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = employee.getId();
        System.out.println("id : " + id);
        employee = employeeService.getById(id);
        Assert.assertNotNull(employee);

        employeeService.updateAfterGet(employee);
        employee = employeeService.getById(id);

        Assert.assertEquals(100, (int) employee.getAge());
    }

    @Test
    public void testUpdate1() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test");
        try {
            // 不会回滚
            employeeService.create(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long id = employee.getId();
        System.out.println("id : " + id);
        employee = employeeService.getById(id);
        Assert.assertNotNull(employee);

        employeeService.updateAfterGet1(employee);
        employee = employeeService.getById(id);

        Assert.assertNotEquals(100, (int) employee.getAge());
    }

    @Test
    public void testCreateAndUpdate() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test-xx");
        employee = employeeService.createThenUpdate(employee);
        Assert.assertNull(employee);
        // employee = employeeService.getById(employee.getId());
        // Assert.assertNotEquals(100, (int) employee.getAge());
    }

    @Test
    public void testCreateAndUpdate1() {
        Random random = new Random();
        Employee employee = new Employee();
        employee.setAge(random.nextInt(60));
        employee.setGender(Gender.FEMALE);
        employee.setName("create-test-xx");
        employee = employeeService.createThenUpdate1(employee);
        Assert.assertNotNull(employee);
        employee = employeeService.getById(employee.getId());
        Assert.assertEquals(100, (int) employee.getAge());
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

}