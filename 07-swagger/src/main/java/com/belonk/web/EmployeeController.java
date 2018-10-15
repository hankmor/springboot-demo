package com.belonk.web;

import com.belonk.common.base.ResultMsg;
import com.belonk.entity.Employee;
import com.belonk.service.EmployeeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sun on 2018/7/2.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(EmployeeController.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private EmployeeService employeeService;

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

    @ApiOperation(value = "获取所有员工")
    @GetMapping
    public ResultMsg queryAll() {
        List<Employee> employees = employeeService.getAll();
        return ResultMsg.success(employees);
    }

    @ApiOperation(value = "获取单个员工", notes = "根据id获取单个员工")
    @ApiImplicitParam(name = "id", value = "员工id", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public ResultMsg get(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        return ResultMsg.success(employee);
    }

    // @ApiIgnore
    @ApiOperation(value = "根据名称获取员工", notes = "根据name模糊查询员工")
    @ApiImplicitParam(name = "name", value = "员工名称", required = true, dataType = "String")
    @GetMapping(value = "/query")
    public ResultMsg getByName(@NotEmpty String name) {
        List<Employee> employees = employeeService.queryByName(name);
        return ResultMsg.success(employees);
    }

    @ApiOperation(value = "新增员工信息", notes = "新增员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "员工名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "gender", value = "性别", dataType = "Integer"),
            @ApiImplicitParam(name = "age", value = "年龄", dataType = "Integer"),
            @ApiImplicitParam(name = "departmentId", value = "所属部分ID", dataType = "Long")
    })
    @PostMapping
    public ResultMsg add(Employee employee) {
        Employee saved = employeeService.save(employee);
        return ResultMsg.success(saved);
    }

    @ApiOperation(value = "更新员工信息", notes = "更新员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "员工名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "gender", value = "性别", dataType = "Integer"),
            @ApiImplicitParam(name = "age", value = "年龄", dataType = "Integer"),
            @ApiImplicitParam(name = "departmentId", value = "所属部分ID", dataType = "Long")
    })
    @PutMapping(value = "/{id}")
    public ResultMsg update(@PathVariable("id") Long id, Employee employee) {
        employee.setId(id);
        return ResultMsg.success(employeeService.save(employee));
    }

    @ApiOperation(value = "删除员工", notes = "根据id删除员工")
    @ApiImplicitParam(name = "id", value = "员工id", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public ResultMsg delete(@PathVariable("id") Long id) {
        employeeService.delete(id);
        return ResultMsg.success(true);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}