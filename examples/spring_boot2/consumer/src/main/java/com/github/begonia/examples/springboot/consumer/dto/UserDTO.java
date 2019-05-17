package com.github.begonia.examples.springboot.consumer.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private String name;

    private Integer age;

    private String hello;

}
