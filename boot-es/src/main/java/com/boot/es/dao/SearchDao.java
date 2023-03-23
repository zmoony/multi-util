package com.boot.es.dao;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

/**
 * 查询接口简单封装
 *
 * @author yuez
 * @version 1.0.0
 * @className SeaechDao
 * @date 2021/3/13 14:49
 **/
public interface SearchDao {
    /**
     * 　　简单条件查询
     * 　　 @param index 索引
     * 　　 @return org.elasticsearch.action.search.SearchResponse
     * 　　 @throws java.io.IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 15:32
     */
    SearchResponse searchDemo(String index) throws IOException;

    /**
     * 　　聚合查询
     * 　　 @param index 索引
     * 　　 @return org.elasticsearch.action.search.SearchResponse
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/13 15:36
     */
    SearchResponse searchAggDemo(String index) throws IOException;


}
