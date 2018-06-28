package com.belonk.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@Data
@EqualsAndHashCode
@NamedQueries({
        @NamedQuery(name = "Employee.findByDeptId", query = "select e from Employee e where e.departmentId = ?1"),
        @NamedQuery(name = "Employee.findByGender1", query = "select e from Employee e where e.gender = ?1"),
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '姓名'")
    private String name;

    @Column(nullable = false, columnDefinition = "bit COMMENT '性别'")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender = Gender.MALE;

    private Integer age;

    private Long departmentId;
}
