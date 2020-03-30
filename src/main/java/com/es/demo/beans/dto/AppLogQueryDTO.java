package com.es.demo.beans.dto;

import lombok.Data;

@Data
public class AppLogQueryDTO {
    private String level;
    private String classname;
    private String thread;
}
