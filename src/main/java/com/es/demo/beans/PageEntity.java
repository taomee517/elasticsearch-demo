package com.es.demo.beans;

import lombok.Data;

import java.util.List;

@Data
public class PageEntity<T> {
    private List<T> data;
    private Long count;
    private Integer pageCount;
}
