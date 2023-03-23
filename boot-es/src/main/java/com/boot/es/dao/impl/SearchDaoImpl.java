package com.boot.es.dao.impl;

import com.boot.es.common.GlobalObject;
import com.boot.es.dao.SearchDao;
import com.boot.es.util.BasicMatchQueryService;
import com.boot.util.dateUtil.DateUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.ZoneId;

/**
 * 查询的简单封装
 * SearchRequest 搜索请求
 * SearchSourceBuilder 条件构造
 * HighlightBuilder 构建高亮
 * TermQueryBuilder 精确查询
 * MatchAllQueryBuilder
 * xxxQueryBuilder 与命令行时的查询类型一一对应
 *
 * @author yuez
 * @version 1.0.0
 * @className SeaechDaoImpl
 * @date 2021/3/13 14:54
 **/
@Repository("seaechDao")
public class SearchDaoImpl implements SearchDao {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResponse searchDemo(String index) throws IOException {
        SearchRequest request = GlobalObject.ES_INFO.isCcs()?
                new SearchRequest(GlobalObject.ES_INFO.getCcsCluster()+":"+index,index):
                new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置Bool Query
        BoolQueryBuilder filterBool =  QueryBuilders.boolQuery();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //设置条件
        //过滤
        boolQueryBuilder.filter(QueryBuilders.termsQuery("tollgate_id", "null"));
        //排除
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("plate_class","1"));
        boolQueryBuilder.mustNot(QueryBuilders.wildcardQuery("plate_class","1*"));
        boolQueryBuilder.mustNot(QueryBuilders.rangeQuery("score").gte(1).lte(2));
        //script
        String script = "doc['pass_time'].value.millis-doc['pass_time2'].value.millis >= 2l"
                +"&& doc['pass_time'].value.millis-doc['pass_time2'].value.millis <=10l";
        boolQueryBuilder.filter(QueryBuilders.scriptQuery(new Script(script)));
        //嵌套查询
        BoolQueryBuilder shouldFilter =  QueryBuilders.boolQuery();
        shouldFilter.should(boolQueryBuilder);
//        shouldFilter.should(boolQueryBuilder2);
        filterBool.filter(shouldFilter);
        //条件构建
        sourceBuilder.query(boolQueryBuilder)
                .from(1)
                .size(10)
                .sort("pass_time");

        request.source(sourceBuilder);
        //短时间添加路由，时间较长时不建议添加，会导致请求超过4K报错
//        request.routing(DateUtils.getRouting("2020-03-01 00:00:00","2020-04-01 00:00:00"));
        return BasicMatchQueryService.requestGet(restHighLevelClient,"searchDemo",request);
    }

    @Override
    public SearchResponse searchAggDemo(String index) throws IOException {
        SearchRequest request = GlobalObject.ES_INFO.isCcs()?
                new SearchRequest(GlobalObject.ES_INFO.getCcsCluster()+":"+index,index):
                new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //...条件设置
        //精确聚合
        TermsAggregationBuilder tollgateAgg = AggregationBuilders
                .terms("tollgate")
                .field("aggField")
                .order(BucketOrder.count(false))
//                .order(BucketOrder.count(false))
                .size(20000);
        //时间聚合
        DateHistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders
                .dateHistogram("countByTime")
                .field("pass_time")
                .calendarInterval(DateHistogramInterval.HOUR)
                .timeZone(ZoneId.systemDefault())
                .format("yyyy-MM-dd HH:mm:ss");
        //去重，只能放在最后一步
        CardinalityAggregationBuilder distinctCount = AggregationBuilders.cardinality("distinctCount")
                .script(new Script("doc['plate_no'].value+ doc['plate_class'].value"));
        //tohit取前几条返回
        TopHitsAggregationBuilder vehicleIdTimeTop = AggregationBuilders.topHits("top")
                .sort("pass_time", SortOrder.DESC)
                .size(1);
        //规约聚合
        SumAggregationBuilder yes = AggregationBuilders
                .sum("yes")
                .script(new Script("doc['plate_no'].value.indexOf('---') == -1"));
        MaxAggregationBuilder pass_time = AggregationBuilders
                .max("3")
                .field("pass_time");
        MinAggregationBuilder pass_time2 = AggregationBuilders
                .min("3")
                .field("pass_time");

        //嵌套聚合
        tollgateAgg.subAggregation(histogramAggregationBuilder);

        sourceBuilder.aggregation(tollgateAgg);
        sourceBuilder.query(boolQueryBuilder).size(0);
        request.source(sourceBuilder);

        return BasicMatchQueryService.requestGet(restHighLevelClient,"searchAggDemo",request);
    }
}
