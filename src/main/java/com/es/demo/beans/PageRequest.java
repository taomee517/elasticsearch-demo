package com.es.demo.beans;

import lombok.Data;

@Data
public class PageRequest{
    private Boolean isPaged;
    private Integer pageNumber;
    private Integer pageSize;
}
