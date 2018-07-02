package com.belonk.dao;

import com.belonk.dao.base.BaseDao;
import com.belonk.entity.Employee;

import java.util.List;

public interface EmployeeDao extends BaseDao<Employee> {
    List<Employee> findByNameLike(String name);
}
