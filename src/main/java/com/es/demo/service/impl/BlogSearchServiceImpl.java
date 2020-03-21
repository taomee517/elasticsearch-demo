package com.es.demo.service.impl;

import com.es.demo.beans.BlogModel;
import com.es.demo.beans.PageEntity;
import com.es.demo.beans.PageRequest;
import com.es.demo.service.IBlogSearchService;
import com.es.demo.utils.PageUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlogSearchServiceImpl implements IBlogSearchService {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<BlogModel> queryByKeyword(String keyword) {
        QueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword,"title", "content");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery)
                .build();
        AggregatedPage<BlogModel> blogModels = elasticsearchTemplate.queryForPage(searchQuery,BlogModel.class);
        return blogModels.getContent();
    }

    @Override
    public PageEntity<List<BlogModel>> queryPageByKeyword(String keyword, PageRequest pageRequest) {
        QueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword,"title", "content");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery)
                .withPageable(PageUtils.buildSpringPageRequest(pageRequest))
                .build();
        AggregatedPage<BlogModel> blogModels = elasticsearchTemplate.queryForPage(searchQuery,BlogModel.class);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setCount(blogModels.getTotalElements());
        pageEntity.setPageCount(blogModels.getTotalPages());
        pageEntity.setData(blogModels.getContent());
        return pageEntity;
    }
}
