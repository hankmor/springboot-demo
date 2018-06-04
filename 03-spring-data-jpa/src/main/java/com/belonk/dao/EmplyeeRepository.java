package com.belonk.dao;

import com.belonk.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmplyeeRepository extends JpaRepository<Employee, String> {
}
