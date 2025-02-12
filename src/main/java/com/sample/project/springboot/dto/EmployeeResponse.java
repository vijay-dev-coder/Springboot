package com.sample.project.springboot.dto;

import lombok.Data;

import java.util.Date;


@Data
public class EmployeeResponse {
    private Long id;

    private String name;
    private int age;
    private double salary;
    private String address;
    private Date dateOfBirth;
}
