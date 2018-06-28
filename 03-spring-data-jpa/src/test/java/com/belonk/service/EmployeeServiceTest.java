package com.belonk.service;

import com.belonk.BaseTest;
import com.belonk.common.util.JsonUtil;
import com.belonk.domain.*;
import com.belonk.entity.Department;
import com.belonk.entity.Employee;
import com.belonk.entity.Gender;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

/**
 * Created by sun on 2018/6/21.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class EmployeeServiceTest extends BaseTest {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(EmployeeServiceTest.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;

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
    public void testQueryByName() {
        String name = "a";
        List<UserConstructWithField> users = employeeService.queryByName(name);
        System.out.println("users : " + users);
        Assert.assertTrue(users.size() == 2);
    }

    @Test
    public void testFindDepartmentById() {
        Long employeeId = 1L;
        Long departmentId = 1L;

        Department destDepartment = departmentService.getById(departmentId);
        Department srcDepartment = employeeService.queryDepartmentOfEmployee(employeeId);
        System.out.println(JsonUtil.toJson(srcDepartment));
        Assert.assertEquals(destDepartment, srcDepartment);
    }

    // top and first query test

    @Test
    public void testTopAndFirst() {
        String name = "test";
        Employee topEmployee = employeeService.queryTopByName(name);
        Assert.assertTrue("test1".equals(topEmployee.getName()));

        List<Employee> top3Employees = employeeService.queryTop3ByName(name);
        Assert.assertTrue(top3Employees.size() == 3);
        Assert.assertTrue("test1".equals(top3Employees.get(0).getName()));
        Assert.assertTrue("test2".equals(top3Employees.get(1).getName()));
        Assert.assertTrue("test3".equals(top3Employees.get(2).getName()));

        Employee firstEmployee = employeeService.queryFirstByName(name);
        Assert.assertTrue("test1".equals(firstEmployee.getName()));

        List<Employee> first3Employees = employeeService.queryFirst3ByName(name);
        Assert.assertTrue(first3Employees.size() == 3);
        Assert.assertTrue("test1".equals(first3Employees.get(0).getName()));
        Assert.assertTrue("test2".equals(first3Employees.get(1).getName()));
        Assert.assertTrue("test3".equals(first3Employees.get(2).getName()));
    }

    // page query test

    @Test
    public void testPageQueryByName() {
        // 条件预设
        int totalSize = 6;
        int pageSize = 4;
        int totalPage = totalSize / pageSize + 1;
        int pageIndex = 0;

        String name = "%test%";
        Page<Employee> employeePage = employeeService.pageQueryByName(pageIndex, pageSize, name);
        System.out.println(JsonUtil.toJson(employeePage));

        // 总页数
        Assert.assertTrue(employeePage.getTotalPages() == totalPage);
        // 总数据量
        Assert.assertTrue(employeePage.getTotalElements() == totalSize);
        // 当前第几页
        Assert.assertTrue(employeePage.getNumber() == pageIndex);
        // 每一页条数
        Assert.assertTrue(employeePage.getSize() == pageSize);
        // 当前页数据条数
        Assert.assertTrue(employeePage.getNumberOfElements() == pageSize);
        // 首页
        Assert.assertTrue(employeePage.isFirst());
        // 非尾页
        Assert.assertTrue(!employeePage.isLast());
        // 还有下一页
        Assert.assertTrue(employeePage.hasNext());

        // 再查一页
        pageIndex++;
        employeePage = employeeService.pageQueryByName(pageIndex, pageSize, name);

        Assert.assertTrue(employeePage.getTotalPages() == totalPage);
        Assert.assertTrue(employeePage.getTotalElements() == totalSize);
        Assert.assertTrue(employeePage.getNumber() == pageIndex);
        Assert.assertTrue(employeePage.getSize() == pageSize);
        Assert.assertTrue(employeePage.getNumberOfElements() == 2);
        Assert.assertTrue(!employeePage.isFirst());
        Assert.assertTrue(employeePage.isLast());
        Assert.assertTrue(!employeePage.hasNext());
    }

    @Test
    public void testPageQueryByAge() {
        // 条件预设
        int pageSize = 4;

        int pageIndex = 0;
        int minAge = 21;

        Slice<Employee> employeePage = employeeService.pageQueryByAge(pageIndex, pageSize, minAge);
        Assert.assertTrue(employeePage.getNumber() == pageIndex);
        Assert.assertTrue(employeePage.getSize() == pageSize);
        Assert.assertTrue(employeePage.getNumberOfElements() == pageSize);
        Assert.assertTrue(employeePage.isFirst());
        Assert.assertTrue(!employeePage.isLast());
        Assert.assertTrue(employeePage.hasNext());

        // 再查一页
        pageIndex++;
        employeePage = employeeService.pageQueryByAge(pageIndex, pageSize, minAge);

        Assert.assertTrue(employeePage.getNumber() == pageIndex);
        Assert.assertTrue(employeePage.getSize() == pageSize);
        Assert.assertTrue(employeePage.getNumberOfElements() == 2);
        Assert.assertTrue(!employeePage.isFirst());
        Assert.assertTrue(employeePage.isLast());
        Assert.assertTrue(!employeePage.hasNext());
    }

    @Test
    public void testPageQueryByNameAndAge() {
        // 条件预设
        int totalSize = 6;
        int pageSize = 4;
        int totalPage = totalSize / pageSize + 1;
        int pageIndex = 0;

        String name = "%test%";
        int minAge = 21;
        Page<Employee> employeePage = employeeService.pageQueryByNameAndAage(pageIndex, pageSize, name, minAge);
        System.out.println(JsonUtil.toJson(employeePage));

        // 总页数
        Assert.assertTrue(employeePage.getTotalPages() == totalPage);
        // 总数据量
        Assert.assertTrue(employeePage.getTotalElements() == totalSize);
        // 当前第几页
        Assert.assertTrue(employeePage.getNumber() == pageIndex);
        // 每一页条数
        Assert.assertTrue(employeePage.getSize() == pageSize);
        // 当前页数据条数
        Assert.assertTrue(employeePage.getNumberOfElements() == pageSize);
        // 首页
        Assert.assertTrue(employeePage.isFirst());
        // 非尾页
        Assert.assertTrue(!employeePage.isLast());
        // 还有下一页
        Assert.assertTrue(employeePage.hasNext());

        // 再查一页
        pageIndex++;
        employeePage = employeeService.pageQueryByNameAndAage(pageIndex, pageSize, name, minAge);

        Assert.assertTrue(employeePage.getTotalPages() == totalPage);
        Assert.assertTrue(employeePage.getTotalElements() == totalSize);
        Assert.assertTrue(employeePage.getNumber() == pageIndex);
        Assert.assertTrue(employeePage.getSize() == pageSize);
        Assert.assertTrue(employeePage.getNumberOfElements() == 2);
        Assert.assertTrue(!employeePage.isFirst());
        Assert.assertTrue(employeePage.isLast());
        Assert.assertTrue(!employeePage.hasNext());
    }

    // stream result test

    @Test
    public void testStream() {
        String name = "%test%";
        int minAge = 23;
        int totalSize = 4;

        List<Employee> employees = employeeService.queryByNameFilterWithAge(name, minAge);
        Assert.assertTrue(employees.size() == totalSize);
        Assert.assertEquals("test3", employees.get(0).getName());
        Assert.assertEquals("test4", employees.get(1).getName());
        Assert.assertEquals("test5", employees.get(2).getName());
        Assert.assertEquals("test6", employees.get(3).getName());
    }

    // async query test

    @Test
    public void testAsyncQuery() {
        List<Employee> employees = employeeService.queryByGenderAsync(Gender.FEMALE);
        Assert.assertEquals(employees.size(), 8);
    }

    // named query test

    @Test
    public void testFindByDeptId() {
        Long deptId = 1L;
        List<Employee> employees = employeeService.queryByDeptId(deptId);
        Assert.assertTrue(employees.size() == 8);
    }

    @Test
    public void testQueryByGender1() {
        Gender gender = Gender.FEMALE;
        List<Employee> employees = employeeService.queryByGender(gender);
        Assert.assertTrue(employees.size() == 8);
    }

    // modifying query test

    @Test
    public void testReverseGender() {
        int rows = employeeService.reverseGenderOfFemale();
        Assert.assertTrue(rows == 8);
    }

    @Test
    public void testDeleteByDeptId() {
        Long deptId = 2L;
        employeeService.deleteByDeptId(deptId);
        List<Employee> employees = employeeService.queryByDeptId(deptId);
        Assert.assertTrue(employees.size() == 0);
    }

    // query projections test

    @Test
    public void testQueryById() {
        Long id = 10L;
        String name = "test6";
        UserConstructWithField user = employeeService.queryById(id);
        Assert.assertEquals(name, user.getName());
    }

    @Test
    public void testQueryByName1() {
        String name = "a";
        List<UserConstructWithField> users = employeeService.queryByName1(name);
        Assert.assertTrue(users.size() == 2);
    }

    @Test
    public void testQueryByIdWithDepartment() {
        Long id = 10L;
        String name = "test6";
        String deptName = "技术部";
        Long deptId = 1L;
        MyEmployee employee = employeeService.queryByIdWithDepartment(id);
        Assert.assertEquals(id, employee.getId());
        Assert.assertEquals(name, employee.getName());
        Assert.assertEquals(deptId, employee.getDepartmentId());
        Assert.assertEquals(deptName, employee.getDepartmentName());
    }

    @Test
    public void testQueryByIdWithDepartmentUseNativeSql() {
        Long id = 10L;
        String name = "test6";
        String deptName = "技术部";
        Long deptId = 1L;
        MyEmployeeDTO employee = employeeService.queryByIdWithDepartmentUseNativeSql(id);
        Assert.assertEquals(id, employee.getId());
        Assert.assertEquals(name, employee.getName());
        Assert.assertEquals(deptId, employee.getDeptId());
        Assert.assertEquals(deptName, employee.getDeptName());
    }

    @Test
    public void testQueryByIdWithDepartmentUserJPQL() {
        Long id = 10L;
        String name = "test6";
        String deptName = "技术部";
        Long deptId = 1L;
        // 直接返回DTO
        MyEmployeeDTO employee = employeeService.queryByIdWithDepartmentUseJPQL(id);
        Assert.assertEquals(id, employee.getId());
        Assert.assertEquals(name, employee.getName());
        Assert.assertEquals(deptId, employee.getDeptId());
        Assert.assertEquals(deptName, employee.getDeptName());
    }

    @Test
    public void testQueryByIdWithDepartmentUserJPQL1() {
        Long id = 10L;
        String name = "test6";
        String deptName = "技术部";
        Long deptId = 1L;
        // 使用动态投影
        MyEmployeeDTO employee = employeeService.queryByIdWithDepartmentUseJPQL1(id);
        Assert.assertEquals(id, employee.getId());
        Assert.assertEquals(name, employee.getName());
        Assert.assertEquals(deptId, employee.getDeptId());
        Assert.assertEquals(deptName, employee.getDeptName());
    }

    @Test
    public void testQueryByDeptId() {
        Long deptId = 2L;
        List<UserConstructWithEmployee> user1s = employeeService.queryByDeptId1(deptId);
        Assert.assertTrue(user1s.size() == 2);
    }

    @Test
    public void testDynamicProjections() {
        int minAge = 21;
        List<UserConstructWithField> users = employeeService.queryByAgeGreaterThan(minAge);
        Assert.assertEquals(5, users.size());
    }

    @Test
    public void testDynamicProjections1() {
        int minAge = 21;
        List<UserConstructWithField1> users = employeeService.queryByAgeGreaterThan1(minAge);
        Assert.assertEquals(5, users.size());
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

}