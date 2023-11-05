package com.yamakassi;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Person {
    private String name;
    private Integer spent;
    private String city;
    private Timestamp timestamp;
}
