package com.es.demo.service;

import com.es.demo.beans.AppLogModel;
import com.es.demo.beans.PageEntity;
import com.es.demo.beans.PageRequest;
import com.es.demo.beans.dto.AppLogQueryDTO;

import java.util.List;

public interface IAppLogSearchService {
    PageEntity<List<AppLogModel>> queryPageByDTO(AppLogQueryDTO appLogQueryDTO, PageRequest pageRequest);
}
