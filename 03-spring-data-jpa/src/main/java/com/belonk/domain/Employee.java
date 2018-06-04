package com.belonk.domain;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee extends Model {
    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '姓名'")
    private String name;

    @Column(nullable = false, columnDefinition = "bit COMMENT '性别'")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender = Gender.MALE;

    @Column(columnDefinition = "int COMMENT '年龄'")
    private Integer age;

    @ManyToOne
    @Column(name = "dept_id")
    private Department department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
