package com.es.demo.jpa;

import com.es.demo.beans.BlogModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BlogRepository extends ElasticsearchRepository<BlogModel, String> {

}
