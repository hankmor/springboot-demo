package com.belonk.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "department")
@Data
@EqualsAndHashCode
public class Department {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Long leaderId;
}
