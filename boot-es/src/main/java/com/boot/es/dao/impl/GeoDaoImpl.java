package com.boot.es.dao.impl;

import com.boot.es.common.GlobalObject;
import com.boot.es.common.ResultCodeEnum;
import com.boot.es.dao.GeoDao;
import com.boot.es.exception.GlobalException;
import com.boot.es.util.BasicMatchQueryService;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.GeoDistanceAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * 地图的工具类
 *
 * @author yuez
 * @version 1.0.0
 * @className GeoDaoImpl
 * @date 2021/3/15 9:32
 **/
@Log4j2
@Repository("geoDao")
public class GeoDaoImpl implements GeoDao {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResponse geoBoundingBox(String index, List<GeoPoint> points) throws IOException, GlobalException {
        if (points == null || points.size() != 2) {
            throw new GlobalException(ResultCodeEnum.UNKNOWN_ERROR.getCode(),"参数只能为两个点位");
        }
        SearchRequest request = GlobalObject.ES_INFO.isCcs() ?
                new SearchRequest(GlobalObject.ES_INFO.getCcsCluster() + ":" + index, index) :
                new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //创建地图点
        GeoPoint geoPointTopLeft = points.get(0);
        GeoPoint geoPointBottomRight = points.get(1);
        //查询条件
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        //过滤条件
        boolQueryBuilder.filter(QueryBuilders.geoBoundingBoxQuery("location")
                .setCornersOGC(geoPointTopLeft, geoPointBottomRight));
        //构建条件
        sourceBuilder.query(boolQueryBuilder).from(1).size(10);
        request.source(sourceBuilder);
        return BasicMatchQueryService.requestGet(restHighLevelClient, "geoBoundingBox", request);
    }

    @Override
    public SearchResponse geoPolygon(String index, List<GeoPoint> points) throws IOException,GlobalException {
        if (points == null || points.size() < 2) {
            throw new GlobalException(ResultCodeEnum.UNKNOWN_ERROR.getCode(),"参数小于两个点位");
        }
        SearchRequest request = GlobalObject.ES_INFO.isCcs() ?
                new SearchRequest(GlobalObject.ES_INFO.getCcsCluster() + ":" + index, index) :
                new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        //过滤条件
        boolQueryBuilder.filter(QueryBuilders.geoPolygonQuery("location",points));
        //构建条件
        sourceBuilder.query(boolQueryBuilder).from(1).size(10);
        request.source(sourceBuilder);
        return BasicMatchQueryService.requestGet(restHighLevelClient, "geoPolygon", request);
    }

    @Override
    public SearchResponse geoDistance(String index, GeoPoint point, String distance) throws IOException {
        SearchRequest request = GlobalObject.ES_INFO.isCcs() ?
                new SearchRequest(GlobalObject.ES_INFO.getCcsCluster() + ":" + index, index) :
                new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        //过滤条件
        boolQueryBuilder.filter(QueryBuilders.geoDistanceQuery("location").point(point).distance(distance, DistanceUnit.KILOMETERS));
        //构建条件
        sourceBuilder.query(boolQueryBuilder).from(1).size(10);
        request.source(sourceBuilder);
        return BasicMatchQueryService.requestGet(restHighLevelClient, "geoDistance", request);
    }

    @Override
    public SearchResponse geoDistanceAgg(String index, GeoPoint point, List<GeoDistanceAggregationBuilder.Range> ranges) throws IOException {
        SearchRequest request = GlobalObject.ES_INFO.isCcs() ?
                new SearchRequest(GlobalObject.ES_INFO.getCcsCluster() + ":" + index, index) :
                new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件...
        //聚合
        GeoDistanceAggregationBuilder aggregationBuilder = AggregationBuilders
                .geoDistance("count_by_distinct", point)
                .field("location")
                .unit(DistanceUnit.METERS)
                .distanceType(GeoDistance.ARC);
        for (GeoDistanceAggregationBuilder.Range range : ranges) {
            aggregationBuilder.addRange(range);
        }
        //构建条件
        sourceBuilder.aggregation(aggregationBuilder);
        sourceBuilder.query(boolQueryBuilder).size(0);
        request.source(sourceBuilder);
        return BasicMatchQueryService.requestGet(restHighLevelClient, "geoDistanceAgg", request);
    }


}
