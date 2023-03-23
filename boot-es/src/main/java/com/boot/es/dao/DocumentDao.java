package com.boot.es.dao;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.core.CountResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 文档的相关操作
 *
 * @author yuez
 * @version 1.0.0
 * @className DocumentDao
 * @date 2021/3/13 14:08
 **/
public interface DocumentDao {
    /**
     * 　　增加文档
     * 　　 @param index 索引
     * 　　 @param id 主键
     * 　　 @param bodyJson 文档内容
     * 　　 @return org.elasticsearch.action.index.IndexResponse
     * 　　 @throws java.io.IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 14:15
     */
    IndexResponse addDocument(String index, String id, String bodyJson) throws IOException;

    /**
     * 　　文档是否存在
     * 　　 @param index 索引
     * 　　 @param type 类型
     * 　　 @param id 主键
     * 　　 @return boolean
     * 　　 @throws java.io.IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 14:17
     */
    boolean existDocument(String index, String type, String id) throws IOException;

    /**
     * 　　获取文档
     *
     * @param index 索引
     * @param type  类型
     * @return org.elasticsearch.action.get.GetResponse
     * @throws java.io.IOException
     * @author yuez
     * @date 2021/3/13 14:18
     */
    GetResponse getDocument(String index, String type, String id) throws IOException;

    /**
     * 　　更新文档
     * 　　 @param index 索引
     * 　　 @param type 类型
     * 　　 @param id 主键
     * 　　 @param bodyJson 文档内容
     * 　　 @return org.elasticsearch.action.update.UpdateResponse
     * 　　 @throws java.io.IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 14:19
     */
    UpdateResponse updateDocument(String index, String type, String id, String bodyJson) throws IOException;

    /**
     * 　　删除文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    主键
     * @return org.elasticsearch.action.delete.DeleteResponse
     * @throws java.io.IOException
     * @author yuez
     * @date 2021/3/13 14:20
     */
    DeleteResponse deleteDocument(String index, String type, String id) throws IOException;

    /**
     * 　　批量插入文档
     *
     * @param index 索引
     * @param type  类型
     * @param map   key为主键ID，value为插入文档
     * @return org.elasticsearch.action.bulk.BulkResponse
     * @throws java.io.IOException
     * @author yuez
     * @date 2021/3/13 14:20
     */
    BulkResponse bulkDocument(String index, String type, Map<String, String> map) throws IOException;

    /**
     * 　　获取文档数量 7.x以后版本支持
     * 　　 @param index 索引
     * 　　 @return org.elasticsearch.client.core.CountResponse
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/15 17:11
     *
     */
    CountResponse documentSize(String index) throws IOException;
}
