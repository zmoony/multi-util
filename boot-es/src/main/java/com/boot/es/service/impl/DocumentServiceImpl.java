package com.boot.es.service.impl;

import com.boot.es.common.R;
import com.boot.es.common.ResultCodeEnum;
import com.boot.es.dao.DocumentDao;
import com.boot.es.service.DocumentService;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * 文档的处理
 *
 * @author yuez
 * @version 1.0.0
 * @className DocumentServiceImpl
 * @date 2021/3/15 17:02
 **/
@Service("documentService")
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentDao documentDao;

    @Override
    public R addDocument(String index, String id, String bodyJson) throws IOException {
        IndexResponse indexResponse = documentDao.addDocument(index, id, bodyJson);
        DocWriteResponse.Result result = indexResponse.getResult();
        if(DocWriteResponse.Result.CREATED == result ||
                DocWriteResponse.Result.UPDATED== result){
            return R.ok().message("操作文档成功");
        }
        return R.error().message("操作文档失败");
    }

    @Override
    public R existDocument(String index, String type, String id) throws IOException {
        boolean b = documentDao.existDocument(index, type, id);
        return b?R.ok().message("存在文档"):R.error().message("文档不存在");
    }

    @Override
    public R getDocument(String index, String type, String id) throws IOException {
        GetResponse document = documentDao.getDocument(index, type, id);
        if(document.isExists()){
            String sourceAsString = document.getSourceAsString();
            return R.ok().data(Collections.singletonList(sourceAsString)).message("获取文档成功");
        }
        return R.error().message("文档不存在");
    }

    @Override
    public R updateDocument(String index, String type, String id, String bodyJson) throws IOException {
        UpdateResponse updateResponse = documentDao.updateDocument(index, type, id, bodyJson);
        DocWriteResponse.Result result = updateResponse.getResult();
        if(DocWriteResponse.Result.CREATED == result ||
                DocWriteResponse.Result.UPDATED== result){
            return R.ok().message("更新文档成功");
        }
        return R.error().message("更新文档失败");
    }

    @Override
    public R deleteDocument(String index, String type, String id) throws IOException {
        DeleteResponse deleteResponse = documentDao.deleteDocument(index, type, id);
        DocWriteResponse.Result result = deleteResponse.getResult();
        if(DocWriteResponse.Result.DELETED == result){
            return R.ok().message("删除成功");
        }else if(DocWriteResponse.Result.NOT_FOUND == result){
            return R.ok().message("未找到相关文档");
        }
        return R.error();
    }

    @Override
    public R bulkDocument(String index, String type, Map<String, String> map) throws IOException {
        BulkResponse bulkItemResponses = documentDao.bulkDocument(index, type, map);
        BulkItemResponse[] items = bulkItemResponses.getItems();
        int failCount = 0;
        int successCount = 0;
        for (BulkItemResponse item : items) {
            if(item.isFailed()){
                failCount++;
                continue;
            }
            successCount++;
        }
        if(successCount >0){
            return R.ok().message("成功"+successCount+"条,失败"+failCount+"条；总数："+items.length);
        }
        return R.error().message("全部失败");
    }

    @Override
    public R documentSize(String index) throws IOException {
        CountResponse countResponse = documentDao.documentSize(index);
        long count = countResponse.getCount();
        return R.ok().data(Collections.singletonList(count));
    }
}
