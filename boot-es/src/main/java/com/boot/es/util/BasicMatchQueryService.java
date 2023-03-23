package com.boot.es.util;

import com.boot.es.common.R;
import com.boot.es.common.ResultCodeEnum;
import com.boot.util.mapUtil.ObjectMapperUtil;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 基本查询通用方法
 *
 * @author yuez
 * @version 1.0.0
 * @className BasicMatchQueryService
 * @date 2021/3/13 16:07
 **/
@Service
@Log4j2
public class BasicMatchQueryService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    /**
     * 　　打印查询语句
     * 　　 @param restHighLevelClient 客户端
     * 　　 @param queryName 所属查询名称
     * 　　 @param request 查询语句
     * 　　 @return org.elasticsearch.action.search.SearchResponse
     * 　　 @throws IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 16:11
     */
    public static SearchResponse requestGet(RestHighLevelClient restHighLevelClient, String queryName, SearchRequest request) throws IOException {
        log.debug(queryName + " 构建的查询：" + request.toString());
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        log.debug(queryName + " 搜索结果：" + searchResponse.toString());
        return searchResponse;
    }

    /**
     * 　　解析数据后统一返回
     * 　　 @param searchResponse 响应体
     * 　　 @param clazz
     * 　　 @return com.boot.es.common.R
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/13 17:01
     *
     */
    public static <T> R buildResponse(SearchResponse searchResponse, Class<T> clazz) throws Exception {
        //超时处理
        if (searchResponse.isTimedOut()) {
            return R.timeOut();
        }
        List<T> list = parseResponse(searchResponse, clazz);
        //有shard处理失败
        if (searchResponse.getFailedShards() > 0) {
            return R.setResult(ResultCodeEnum.FAILEDSHARDS).data(list);
        }
        return R.ok().data(list);
    }


    /**
     * 　　处理ES返回的数据，封装
     */
    private static <T> List<T> parseResponse(SearchResponse searchResponse, Class<T> clazz) throws Exception {
        LinkedList<T> list = new LinkedList<>();
        SearchHit[] hits = searchResponse.getHits().getHits();
        if (hits != null) {
            for (SearchHit hit : hits) {
                T o = ObjectMapperUtil.json2pojo(hit.getSourceAsString(), clazz);
                list.add(o);
            }
        }
        return list;
    }




}
