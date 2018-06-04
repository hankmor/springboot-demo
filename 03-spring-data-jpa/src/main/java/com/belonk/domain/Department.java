package com.belonk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "department")
public class Department extends Model {
    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '名称'")
    private String name;

    @Column
    private Employee leader;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
