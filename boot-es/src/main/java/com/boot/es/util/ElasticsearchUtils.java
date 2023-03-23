package com.boot.es.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Elasticsearch操作类
 * @author 54231
 */
@Slf4j
@Component
public class ElasticsearchUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static RestHighLevelClient client;

    @Resource
    public void setRestHighLevelClient(RestHighLevelClient client) {
        log.info(" es init data {}",client);
        if (null == client) {
            log.info("Elasticsearch初始化配置失败，请检查配置项");
        } else {
            log.info("Elasticsearch初始化配置注入成功！");
        }
        ElasticsearchUtils.client = client;
    }

    /**
     * 创建索引
     *
     * @param index 索引
     * @param shards 分片
     * @param replicas 副本
     */
    public static void createVehicleIndex(String index, int shards, int replicas) throws IOException {
        if (!checkIndexExist(index)) {
            log.info("create vehicle index {} start", index);
            CreateIndexRequest request = new CreateIndexRequest(index);
            // 设置参数：分片和副本
            request.settings(Settings.builder().put("number_of_shards", shards).put("number_of_replicas", replicas));
            client.indices().create(request, RequestOptions.DEFAULT);
            //为排序字段创建mapping
            XContentBuilder mappingBuilder= JsonXContent.contentBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("passId")
                    .field("type","keyword")
                    .endObject()
                    .startObject("equipmentId")
                    .field("type","keyword")
                    .endObject()
                    .startObject("tollgateId")
                    .field("type","keyword")
                    .endObject()
                    .startObject("laneNo")
                    .field("type","short")
                    .endObject()
                    .startObject("villageCode")
                    .field("type","keyword")
                    .endObject()
                    .startObject("enterOrOut")
                    .field("type","short")
                    .endObject()
                    .startObject("infoAssort")
                    .field("type","short")
                    .endObject()
                    .startObject("sourceId")
                    .field("type","short")
                    .endObject()
                    .startObject("dataSource")
                    .field("type","short")
                    .endObject()
                    .startObject("dataSourceFactory")
                    .field("type","short")
                    .endObject()
                    .startObject("platePicUrl")
                    .field("type","keyword")
                    .endObject()
                    .startObject("carUrl")
                    .field("type","keyword")
                    .endObject()
                    .startObject("senceUrl")
                    .field("type","keyword")
                    .endObject()
                    .startObject("imageFormat")
                    .field("type","integer")
                    .endObject()
                    .startObject("leftTopX")
                    .field("type","integer")
                    .endObject()
                    .startObject("leftTopY")
                    .field("type","integer")
                    .endObject()
                    .startObject("rightBtmX")
                    .field("type","integer")
                    .endObject()
                    .startObject("rightBtmY")
                    .field("type","integer")
                    .endObject()
                    .startObject("plateLeftTopX")
                    .field("type","integer")
                    .endObject()
                    .startObject("plateLeftTopY")
                    .field("type","integer")
                    .endObject()
                    .startObject("plateRightBtmX")
                    .field("type","integer")
                    .endObject()
                    .startObject("plateRightBtmY")
                    .field("type","integer")
                    .endObject()
                    .startObject("faceNum")
                    .field("type","short")
                    .endObject()
                    .startObject("carAnnualInspectionNum")
                    .field("type","short")
                    .endObject()
                    .startObject("pendantsNum")
                    .field("type","short")
                    .endObject()
                    .startObject("ornamentsNum")
                    .field("type","short")
                    .endObject()
                    .startObject("tissueBoxNum")
                    .field("type","short")
                    .endObject()
                    .startObject("passTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("pushTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("entryTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("wholeConfidence")
                    .field("type","double")
                    .endObject()
                    .startObject("plateNo")
                    .field("type","keyword")
                    .endObject()
                    .startObject("plateNumConfidence")
                    .field("type","double")
                    .endObject()
                    .startObject("plateClass")
                    .field("type","short")
                    .endObject()
                    .startObject("plateColor")
                    .field("type","short")
                    .endObject()
                    .startObject("driveStatus")
                    .field("type","keyword")
                    .endObject()
                    .startObject("carType")
                    .field("type","keyword")
                    .endObject()
                    .startObject("carColor")
                    .field("type","short")
                    .endObject()
                    .startObject("carBrand")
                    .field("type","short")
                    .endObject()
                    .startObject("carBrandConfidence")
                    .field("type","double")
                    .endObject()
                    .startObject("carYear")
                    .field("type","keyword")
                    .endObject()
                    .startObject("carYearConfidence")
                    .field("type","double")
                    .endObject()
                    .startObject("carSubBrand")
                    .field("type","keyword")
                    .endObject()
                    .startObject("carSubBrandConfidence")
                    .field("type","double")
                    .endObject()
                    .startObject("carSpeed")
                    .field("type","integer")
                    .endObject()
                    .startObject("markerType")
                    .field("type","keyword")
                    .endObject()
                    .startObject("carHeadend")
                    .field("type","short")
                    .endObject()
                    .startObject("shieldFace")
                    .field("type","short")
                    .endObject()
                    .startObject("algorithmVersion")
                    .field("type","keyword")
                    .endObject()
                    .startObject("algorithmVendor")
                    .field("type","short")
                    .endObject()
                    .startObject("features")
                    .field("type","keyword")
                    .endObject()
                    .startObject("isSecondaryStructure")
                    .field("type","short")
                    .endObject()
                    .startObject("structureTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("structureNum")
                    .field("type","short")
                    .endObject()
                    .startObject("moveDirection")
                    .field("type","keyword")
                    .endObject()
                    .startObject("specialCar")
                    .field("type","short")
                    .endObject()
                    .startObject("roofItems")
                    .field("type","short")
                    .endObject()
                    .startObject("plateOcclusion")
                    .field("type","integer")
                    .endObject()
                    .startObject("facialOcclusion")
                    .field("type","short")
                    .endObject()
                    .endObject()
                    .endObject();
            PutMappingRequest putMappingRequest = new PutMappingRequest(index);
            putMappingRequest.source(mappingBuilder);
            client.indices().putMapping(putMappingRequest,RequestOptions.DEFAULT);
        }
    }

    /**
     * 创建索引
     *
     * @param index 索引
     * @param shards 分片
     * @param replicas 副本
     */
    public static void createPeopleIndex(String index, int shards, int replicas) throws IOException {
        if (!checkIndexExist(index)) {
            log.info("create people index {} start", index);
            CreateIndexRequest request = new CreateIndexRequest(index);
            // 设置参数：分片和副本
            request.settings(Settings.builder().put("number_of_shards", shards).put("number_of_replicas", replicas));
            client.indices().create(request, RequestOptions.DEFAULT);
            //为排序字段创建mapping
            XContentBuilder mappingBuilder= JsonXContent.contentBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("passId")
                    .field("type","keyword")
                    .endObject()
                    .startObject("equipmentId")
                    .field("type","keyword")
                    .endObject()
                    .startObject("villageCode")
                    .field("type","keyword")
                    .endObject()
                    .startObject("infoAssort")
                    .field("type","short")
                    .endObject()
                    .startObject("sourceId")
                    .field("type","short")
                    .endObject()
                    .startObject("sourceType")
                    .field("type","short")
                    .endObject()
                    .startObject("dataSourceFactory")
                    .field("type","short")
                    .endObject()
                    .startObject("faceImg")
                    .field("type","keyword")
                    .endObject()
                    .startObject("sceneImg")
                    .field("type","keyword")
                    .endObject()
                    .startObject("imageFormat")
                    .field("type","short")
                    .endObject()
                    .startObject("leftTopX")
                    .field("type","integer")
                    .endObject()
                    .startObject("leftTopY")
                    .field("type","integer")
                    .endObject()
                    .startObject("rightBtmX")
                    .field("type","integer")
                    .endObject()
                    .startObject("rightBtmY")
                    .field("type","integer")
                    .endObject()
                    .startObject("passTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("pushTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("startTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("endTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("entryTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("confidence")
                    .field("type","double")
                    .endObject()
                    .startObject("age")
                    .field("type","short")
                    .endObject()
                    .startObject("ageSegment")
                    .field("type","short")
                    .endObject()
                    .startObject("gender")
                    .field("type","short")
                    .endObject()
                    .startObject("nation")
                    .field("type","short")
                    .endObject()
                    .startObject("skinColor")
                    .field("type","short")
                    .endObject()
                    .startObject("eyeglass")
                    .field("type","short")
                    .endObject()
                    .startObject("sunglass")
                    .field("type","short")
                    .endObject()
                    .startObject("mask")
                    .field("type","short")
                    .endObject()
                    .startObject("headMarker")
                    .field("type","text")
                    .endObject()
                    .startObject("moveDirection")
                    .field("type","keyword")
                    .endObject()
                    .startObject("moveSpeed")
                    .field("type","integer")
                    .endObject()
                    .startObject("algorithmVersion")
                    .field("type","keyword")
                    .endObject()
                    .startObject("algorithmVendor")
                    .field("type","short")
                    .endObject()
                    .startObject("feature")
                    .field("type","keyword")
                    .endObject()
                    .startObject("featureId")
                    .field("type","keyword")
                    .endObject()
                    .startObject("isSecondaryStructure")
                    .field("type","short")
                    .endObject()
                    .startObject("structureTime")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("driverFlag")
                    .field("type","short")
                    .endObject()
                    .startObject("vid")
                    .field("type","keyword")
                    .endObject()
                    .startObject("isIdentify")
                    .field("type","short")
                    .endObject()
                    .startObject("deleteFlag")
                    .field("type","short")
                    .endObject()
                    .startObject("traitLocation")
                    .field("type","keyword")
                    .endObject()
                    .endObject()
                    .endObject();
            PutMappingRequest putMappingRequest = new PutMappingRequest(index);
            putMappingRequest.source(mappingBuilder);
            client.indices().putMapping(putMappingRequest,RequestOptions.DEFAULT);
        }
    }

    /**
     * 查询所有的索引
     * @return
     */
    public static List<String> queryAllIndex(){
        try {
            Request request = new Request("get", "/_cat/indices?v");
            Response response = client.getLowLevelClient().performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            List<String> allIndexs = getStringList(responseBody, "index");
            List<String> list = new ArrayList();
            Iterator var7 = allIndexs.iterator();
            while(var7.hasNext()) {
                String index = (String)var7.next();
                if (!index.startsWith(".")) {
                    list.add(index);
                }
            }
            return list;
        } catch (Exception var8) {
            var8.printStackTrace();
            return getIndicesFromState();
        }
    }

    /**
     * 根据索引前缀查询所有的索引
     * @param indexHeader
     * @return
     */
    public static List<String> queryIndexByIndexHeader(String indexHeader){
        List<String> resultList = new ArrayList<>();
        List<String> list = queryAllIndex();
        for(String str : list){
            if(str.startsWith(indexHeader)){
                resultList.add(str);
            }
        }
        return resultList;
    }

    /**
     * 检查索引是否存在
     *
     * @param index 索引
     * @return 是否
     */
    public static boolean checkIndexExist(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest(index);
            request.indices();
            //            log.info("existsIndex: " + exist);
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     * @throws IOException
     */
    public static boolean delIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        return deleteIndexResponse.isAcknowledged();
    }

    /**
     * 单个文档添加
     * @param index
     * @param data
     * @throws Exception
     */
    public static void addDocument(String index, Object data) throws Exception {
        //因为时7.0.0版本的es所以不能使用有类型的构造器
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(mapper.writeValueAsString(data), XContentType.JSON);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 批量添加文档
     *
     * @param index
     * @param list
     * @throws Exception
     */
    public static void addDocumentList(String index, List<Object> list) throws Exception {
        log.info("add documentList in {}", index);
        BulkRequest request = new BulkRequest();
        for (Object object : list) {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.source(mapper.writeValueAsString(object), XContentType.JSON);
            request.add(indexRequest);
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 单个删除文档
     *
     * @param index
     * @param id
     * @return
     * @throws IOException
     */
    public static void delDocument(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index).id(id);
        client.delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 根据Id数组查询
     *
     * @param index
     * @param type
     * @param ids
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static SearchHits termQueryByIds(String index, String type, int current, int size, Object[] ids)
            throws IOException, ParseException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        setPage(current, size, searchSourceBuilder);
        // 搜索方式
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

    /**
     * 分页全局搜索
     *
     * @param index
     * @param current 当前页
     * @param size 每页条数
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static SearchHits searchAllByPage(String index, int current, int size)
            throws IOException, ParseException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        setPage(current, size, searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 处理返回结果
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

    /**
     * 多条件交集查询
     * @param index
     * @param current
     * @param size
     * @param field 查询字段
     * @param params 查询参数
     * @return
     * @throws Exception
     */
    public static SearchHits getListByParamsForAnd(String index, int current, int size, String field, List<String> params)
            throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String param : params) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(field, param);
            boolQueryBuilder.must(termQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
        }
        setPage(current, size, searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

    /**
     * 多条件交集查询
     * @param index
     * @param current
     * @param size
     * @param params 查询参数
     * @return
     * @throws Exception
     */
    public static Map<String,Object> getListByParamMap(String index, int current, int size, Map<String,String> params, String selectType)
            throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        params.forEach((key,value)->{
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key, value);
            if("and".equals(selectType)){
                boolQueryBuilder.must(termQueryBuilder);
            }else{
                boolQueryBuilder.should(termQueryBuilder);
            }
            searchSourceBuilder.query(boolQueryBuilder);
        });
        CountRequest countRequest=new CountRequest();
        countRequest.source(searchSourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long count = countResponse.getCount();
        if (count==0){
          //  throw new CheckFailedException("ES中没有打过标签的对象信息");
        }
        setPage(current, size, searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        Map<String,Object> map = new HashMap<>();
        map.put("total",count);
        map.put("hits",hits);
        return map;
    }

    /**
     * 多条件并集查询
     * @param index
     * @param current
     * @param size
     * @param field 搜索字段
     * @param params 查询参数
     * @return
     * @throws Exception
     */
    public static SearchHits getListByParamsForShould(String index, int current, int size, String field, List<String> params) throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        for (String param : params) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(field, param);
            boolQueryBuilder.should(termQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
        }
        setPage(current, size, searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

    /**
     * 查询文档总数
     * @param index
     * @return
     * @throws IOException
     */
    public static Long countQuery(String index) throws IOException {
        //处理BoolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("passTime").gte("2020-01-01 00:00:00").lte("2020-10-10 00:00:00"));
        CountRequest countRequest = new CountRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        countRequest.source(searchSourceBuilder);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long count = countResponse.getCount();
        log.info("查询条件:{}",searchSourceBuilder.toString());
        return count;
    }

    private static void setPage(int current, int size, SearchSourceBuilder searchSourceBuilder) {
        if(current==0){
            current=1;
        }
        //设置分页
        if(size!=0){
            searchSourceBuilder.from((current - 1) * size)
                    .size(size);
        }
    }

    /**
     * 批量添加数据
     * @param index
     * @param list
     * @throws Exception
     */
    public static void synDocumentList(String index, List<Map<String,Object>> list) throws Exception {
        BulkRequest request = new BulkRequest();
        for(Map<String,Object> map : list){
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.source(mapper.writeValueAsString(map), XContentType.JSON);
            request.add(indexRequest);
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 清空index下所有数据
     * @param index
     * @throws Exception
     */
    public static void deleteData(String index) throws Exception {
        //查询所有数据
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 处理返回结果
        SearchHits hits = searchResponse.getHits();
        List<String> docIds = new ArrayList<>(hits.getHits().length);
        for (SearchHit hit : hits) {
            docIds.add(hit.getId());
        }
        if(docIds!=null&&docIds.size()>0){
            //根据文档id删除所有数据
            BulkRequest bulkRequest = new BulkRequest();
            for (String id : docIds) {
                //DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
                DeleteRequest deleteRequest = new DeleteRequest(index).id(id);
                bulkRequest.add(deleteRequest);
            }
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        }
    }

    /**
     * 根据标签进行多条查询（eq-相同 neq-不同）
     * @param index
     * @param type
     * @param current
     * @param size
     * @param params
     * @return
     * @throws Exception
     */
    public static SearchHits getListByParamsForMustAnd(String index, String type, int current, int size, List<Map> params)
            throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        //searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map param : params) {
            if("eq".equals(param.get("operator").toString())){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(param.get("tagId").toString(), param.get("tagVal").toString());
                boolQueryBuilder.must(termQueryBuilder);
                searchSourceBuilder.query(boolQueryBuilder);
            }
            if("neq".equals(param.get("operator").toString())){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(param.get("tagId").toString(), param.get("tagVal").toString());
                boolQueryBuilder.mustNot(termQueryBuilder);
                searchSourceBuilder.query(boolQueryBuilder);
            }
        }
        setPage(current, size, searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

    /**
     * 多条件并集查询 不分页
     * @param index
     * @param filed 搜索字段
     * @param params 查询参数
     * @return
     * @throws Exception
     */
    public static SearchHits getAllListByParamsForShould(String index, String filed, List<String> params) throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        for (String param : params) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(filed, param);
            boolQueryBuilder.should(termQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

    /**
     * 根据documentId批量删除对应文档
     * @param index
     * @param docIdList
     * @throws Exception
     */
    public static void deleteByDocumentIds(String index, List<String> docIdList) throws Exception {
        //根据文档id删除数据
        BulkRequest bulkRequest = new BulkRequest();
        for (String docId : docIdList) {
            DeleteRequest deleteRequest = new DeleteRequest(index).id(docId);
            bulkRequest.add(deleteRequest);
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * 根据标签id和value分页查询
     * @param index
     * @param current
     * @param size
     * @param paramsList
     * @return
     * @throws Exception
     */
    public static SearchHits getDataListByParamsForShould(String index, int current, int size, List<Map<String,String>> paramsList)
            throws Exception {
        SearchRequest searchRequest = new SearchRequest(index);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        for (Map<String,String> param : paramsList) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(param.get("tagId"), param.get("tagValue"));
            boolQueryBuilder.should(termQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
        }
        setPage(current, size, searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

    /**
     * 解析返回的数据
     * @param responseBody
     * @param cloumnName
     * @return
     */
    public static List<String> getStringList(String responseBody, String cloumnName) {
        List<String> reList = new ArrayList();
        if (responseBody != null && !responseBody.isEmpty()) {
            String[] list = responseBody.split("\n");
            String titleStr = list[0];
            titleStr = titleStr.trim().replaceAll(" +", ",");
            String[] titles = titleStr.split(",");
            List<JSONObject> dataList = new ArrayList();
            for(int i = 1; i < list.length; ++i) {
                String dataStr = list[i];
                dataStr = dataStr.trim().replaceAll(" +", ",");
                String[] datas = dataStr.split(",");
                JSONObject json = new JSONObject();
                for(int j = 0; j < titles.length; ++j) {
                    json.put(titles[j], datas[j]);
                }
                dataList.add(json);
            }
            Iterator var14 = dataList.iterator();
            while(var14.hasNext()) {
                JSONObject data = (JSONObject)var14.next();
                reList.add(data.getStr(cloumnName));
            }
        }
        return reList;
    }

    public static List<String> getIndicesFromState() {
        List<String> list = new ArrayList();
        /*JSONObject json = getClusterState();
        JSONObject indices = json.getJSONObject("metadata").getJSONObject("indices");
        if (indices != null) {
            Iterator var5 = indices.keySet().iterator();
            while(var5.hasNext()) {
                String index = (String)var5.next();
                list.add(index);
                List<String> aliases = (List)indices.getJSONObject(index).get("aliases");
                if (aliases != null) {
                    list.addAll(aliases);
                }
            }
        }*/
        return list;
    }

    /**
     * 获取集群状态
     * @return
     */
    public static String getClusterState() {
        try {
            Request request = new Request("get", "/_cluster/state");
            Response response = client.getLowLevelClient().performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    /**
     * ES滚动数据查询
     * @param size 展示数量
     * @param index 下表
     * @param indices 查询字段
     * @param <T>
     * @return
     */
    public static <T> List<T> scrollQuery(int size, String index,String [] indices){
       return scrollQuery(size, index,indices,5L);
    }


    /**
     * ES滚动数据查询
     * @param size
     * @param indices 构造器加入需要查找的字段
     * @param scroll_time 快照默认查询时间
     * @return
     * @param <T>
     */
    public static <T> List<T> scrollQuery(int size, String index, String[] indices, Long scroll_time){

        //存储scroll的list
        List<String> scrollIdList = new ArrayList<>();

        try {
            //滚动查询的Scroll
            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(scroll_time));
            //构建搜索条件
            SearchRequest request =new SearchRequest(index);
            //SearchRequest request =new SearchRequest(indices);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            if(indices != null){
                //构造器加入需要查找的字段
                sourceBuilder.fetchSource(indices, null);
            }
            //加入Query语句

            sourceBuilder.size(size);

            request.scroll(scroll);
            request.source(sourceBuilder);

            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

            String scrollId = searchResponse.getScrollId();

            SearchHit[] hits = searchResponse.getHits().getHits();

            scrollIdList.add(scrollId);
            while (ArrayUtils.isNotEmpty(hits)){
                log.info("xx size {}",hits.length);
                CopyOptions copyOptions = new CopyOptions();
                for(SearchHit hit : hits){
                    /*DossierPersonBean bean = BeanUtil.mapToBean(hit.getSourceAsMap(), DossierPersonBean.class, true, copyOptions);
                    if(bean != null){
                        ResourceEnum resourceEnum = ResourceEnum.bzdzdxb;
                        String conditon = resourceEnum.getCondition()+" test ";
                        String result = HttpViewDocUtils.sendYCSJInter(resourceEnum,conditon);
                        HttpViewDocUtils.postQueryListNotCache(result,DossierPersonBean.class);
                    }*/
                }
                if(hits.length < size){
                    break;
                }
                //继续滚动,根据上个游标，得到这次开始查询的位置
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
                searchScrollRequest.scroll(scroll);
                //得到结果
                SearchResponse resp = client.scroll(searchScrollRequest, RequestOptions.DEFAULT);
                //定位游标
                scrollId = resp.getScrollId();
                hits = resp.getHits().getHits();
                scrollIdList.add(scrollId);
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //清理scroll，释放资源
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.setScrollIds(scrollIdList);
            try {
                client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            } catch (IOException ignored) {
            }
        }
        return null;
    }

}
