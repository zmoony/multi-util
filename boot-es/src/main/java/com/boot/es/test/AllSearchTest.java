package com.boot.es.test;

import com.boot.es.common.GlobalObject;
import com.boot.es.common.R;
import com.boot.es.util.BasicMatchQueryService;
import com.boot.es.util.ElasticsearchUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全文检索
 *
 * @author yuez
 * @since 2022/9/7
 */
@SpringBootTest
public class AllSearchTest {
    private final static String index="index_basicperson";

    @Test
    public void allColumnSearch() throws Exception {
        String content = "群众";
        //todo 进行分词
        
        Map<String,Float> fields = new HashMap<>();//加权重筛选
//        fields.put("phone",1.0f);
        RestHighLevelClient client = ElasticsearchUtils.client;
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(content)
                .fields(fields)
//                .analyzer("standard")
                .fuzziness(Fuzziness.AUTO)
                .type(MultiMatchQueryBuilder.Type.MOST_FIELDS);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.from(0).size(10);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println(response);
        R r = BasicMatchQueryService.buildResponse(response, Map.class);
        System.out.println(r);
    }





}
