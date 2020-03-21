package com.es.demo.jpa;

import com.es.demo.beans.BlogModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BlogRepository extends ElasticsearchRepository<BlogModel, String> {

    /**
     * 查询标题匹配关键字的文档
     * @param keyword
     * @return
     */
    List<BlogModel> findByTitleLike(String keyword);


    /**
     * 查询内容与关键字匹配的文档
     * @param keyword
     * @return
     */
    List<BlogModel> findByContentLike(String keyword);
}
