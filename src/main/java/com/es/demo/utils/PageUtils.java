package com.es.demo.utils;

import com.es.demo.beans.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtils {
    public static Pageable buildSpringPageRequest(PageRequest request){
        if (request.getIsPaged()) {
            return org.springframework.data.domain.PageRequest.of(request.getPageNumber()-1,request.getPageSize());
        }
        return null;
    }
}
