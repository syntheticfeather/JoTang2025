package com.example.demo.entity;

import java.util.List;

import lombok.Data;

@Data
public class PageBean<T> {

    private Long total;
    private List<T> items;

}

