package com.es.demo.service;

import com.es.demo.beans.BlogModel;
import com.es.demo.beans.PageEntity;
import com.es.demo.beans.PageRequest;

import java.util.List;

public interface IBlogSearchService {
    List<BlogModel> queryByKeyword(String keyword);

    PageEntity<List<BlogModel>> queryPageByKeyword(String keyword, PageRequest pageRequest);
}
