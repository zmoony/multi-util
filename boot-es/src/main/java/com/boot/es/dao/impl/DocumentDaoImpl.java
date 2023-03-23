package com.boot.es.dao.impl;

import com.boot.es.dao.DocumentDao;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

/**
 * 文档操作实现类
 *
 * @author yuez
 * @version 1.0.0
 * @className DocumentDaoImpl
 * @date 2021/3/13 14:22
 **/
@Repository("documentDao")
public class DocumentDaoImpl implements DocumentDao {
    //超时时间
    public static final TimeValue TIME_OUT = TimeValue.timeValueSeconds(10L);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public IndexResponse addDocument(String index, String id, String bodyJson) throws IOException {
        IndexRequest request = new IndexRequest(index);
        //规则 put /index/_doc/id
        request.id(id);
        //设置超时事件
        request.timeout(TIME_OUT);
        //放入消息体
        request.source(bodyJson, XContentType.JSON);
        //客户端发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return response;
    }

    @Override
    public boolean existDocument(String index, String type, String id) throws IOException {
        GetRequest request = new GetRequest(index, type, id);
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }

    @Override
    public GetResponse getDocument(String index, String type, String id) throws IOException {
        GetRequest request = new GetRequest(index, type, id);
        return restHighLevelClient.get(request, RequestOptions.DEFAULT);
    }

    @Override
    public UpdateResponse updateDocument(String index, String type, String id, String bodyJson) throws IOException {
        UpdateRequest request = new UpdateRequest(index, type, id);
        request.timeout(TIME_OUT);
        request.doc(bodyJson, XContentType.JSON);
        return restHighLevelClient.update(request, RequestOptions.DEFAULT);
    }

    @Override
    public DeleteResponse deleteDocument(String index, String type, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        deleteRequest.timeout(TIME_OUT);
        return restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @Override
    public BulkResponse bulkDocument(String index, String type, Map<String, String> map) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TIME_OUT);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            bulkRequest.add(new IndexRequest(index, type, entry.getKey())
                    .source(entry.getValue(), XContentType.JSON));
        }
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Override
    public CountResponse documentSize(String index) throws IOException {
        CountRequest countRequest = new CountRequest(index);
//        countRequest.query(QueryBuilders.matchAllQuery());
        return restHighLevelClient.count(countRequest,RequestOptions.DEFAULT);
    }
}
