package com.boot.es.dao.impl;

import com.boot.es.dao.IndexDao;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * index操作实现类
 * @author yuez
 * @version 1.0.0
 * @className IndexDaoImpl
 * @date 2021/3/13 13:41
 **/
@Repository("indexDao")
public class IndexDaoImpl implements IndexDao {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean delIndex(String index) throws IOException {
        //操作索引的对象
        IndicesClient indices = restHighLevelClient.indices();
        //删除索引的请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        //删除索引
        AcknowledgedResponse response = indices.delete(deleteIndexRequest, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    @Override
    public boolean addIndex(String index,String type , Integer numberOfShards, Integer numberOfReplicas, String propertiesJson) throws IOException {
        //操作索引的对象
        IndicesClient indices = restHighLevelClient.indices();
        //创建索引请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.settings(Settings.builder().put("number_of_shards", numberOfShards)
                .put("number_of_replicas", numberOfReplicas));
        //创建映射
        createIndexRequest.mapping(type,propertiesJson, XContentType.JSON);
        //执行创建语句
        CreateIndexResponse response = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    @Override
    public boolean existIndex(String index) throws IOException {
        IndicesClient indices = restHighLevelClient.indices();
        GetIndexRequest request = new GetIndexRequest().indices(index);
        return indices.exists(request, RequestOptions.DEFAULT);
    }

}
