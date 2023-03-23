package com.boot.es.service;

import com.boot.es.common.R;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.core.CountResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 文档的后续处理
 *
 * @author yuez
 * @version 1.0.0
 * @className DocumentService
 * @date 2021/3/15 10:19
 **/
public interface DocumentService {
    R addDocument(String index, String id, String bodyJson) throws IOException;
    R existDocument(String index, String type, String id) throws IOException;
    R getDocument(String index, String type, String id) throws IOException;
    R updateDocument(String index, String type, String id, String bodyJson) throws IOException;
    R deleteDocument(String index, String type, String id) throws IOException;
    R bulkDocument(String index, String type, Map<String, String> map) throws IOException;
    R documentSize(String index) throws IOException;
}
