package com.es.demo.service.impl;

import com.es.demo.beans.AppLogModel;
import com.es.demo.beans.PageEntity;
import com.es.demo.beans.PageRequest;
import com.es.demo.beans.dto.AppLogQueryDTO;
import com.es.demo.service.IAppLogSearchService;
import com.es.demo.utils.PageUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AppLogSearchServiceImpl implements IAppLogSearchService {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public PageEntity<List<AppLogModel>> queryPageByDTO(AppLogQueryDTO appLogQueryDTO, PageRequest pageRequest) {
        Pageable pageable = PageUtils.buildSpringPageRequest(pageRequest);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(appLogQueryDTO.getLevel())){
            QueryBuilder termQueryBuilder = QueryBuilders.termQuery("level",appLogQueryDTO.getLevel());
            boolQueryBuilder.must(termQueryBuilder);
        }

//        if(!StringUtils.isEmpty(appLogQueryDTO.getClassname())){
//            QueryBuilder matchQuery = QueryBuilders.matchQuery("classname", appLogQueryDTO.getClassname());
//            boolQueryBuilder.must(matchQuery);
//        }

        if(!StringUtils.isEmpty(appLogQueryDTO.getThread())){
            QueryBuilder matchQuery = QueryBuilders.matchQuery("thread", appLogQueryDTO.getThread());
            boolQueryBuilder.filter(matchQuery);
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();

        AggregatedPage<AppLogModel> appLogModels = elasticsearchTemplate.queryForPage(searchQuery,AppLogModel.class);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setCount(appLogModels.getTotalElements());
        pageEntity.setPageCount(appLogModels.getTotalPages());
        pageEntity.setData(appLogModels.getContent());
        return pageEntity;
    }
}
