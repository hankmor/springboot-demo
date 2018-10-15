package com.belonk.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@Data
@EqualsAndHashCode
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '姓名'")
    @NotEmpty
    private String name;

    @Column(nullable = false, columnDefinition = "bit COMMENT '性别'")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender = Gender.MALE;

    private Integer age;

    private Long departmentId;
}
