package com.es.demo.beans;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Accessors(chain = true)
@Document(indexName = "kafka-message", type = "_doc")
public class AppLogModel {

    private String id;

    private String classname;

    private String thread;

    private String level;

    private String content;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String timestamp;
}
