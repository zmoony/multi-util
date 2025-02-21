package com.boot.es.test;

import org.apache.http.HttpHost;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * DateTimeTest
 *
 * @author yuez
 * @since 2024/2/1
 */
public class DateTimeTest {

    String index = "index_date_test";
    String url = "172.18.12.21:9312";
    @Test
    public void health() throws IOException {
        RestHighLevelClient connection = getConnection();
        ClusterHealthResponse health = connection.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
        System.out.println(health.toString());
    }

    /**
     * "hits" : [
     *       {
     *         "_index" : "index_date_test",
     *         "_type" : "_doc",
     *         "_id" : "2024",
     *         "_score" : 1.0,
     *         "_routing" : "20240201",
     *         "_source" : {
     *           "pass_time" : "2024/02/01 12:00:00",
     *           "time" : "2024-02-01T12:00:00.000Z"
     *         }
     *       }
     *     ]
     * @throws IOException
     */
    @Test
    public void insert() throws IOException, ParseException {
        String id = "2022";
        String routing = "20240201";
        String time = "2024/02/01 12:00:00";
        Map<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("pass_time", time);
    //        jsonMap.put("time",LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));  // "time" : "2024-02-01T12:00:00.000Z"
        jsonMap.put("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time)); //"time" : "2024-02-01T04:00:00.000Z" 少8小时

        RestHighLevelClient connection = getConnection();
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(id);
        indexRequest.routing(routing);
        indexRequest.source(jsonMap);
        connection.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Test
    public void read() throws IOException, ParseException {
        String time = "2024/02/01 12:00:00";
        Date parse = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        /*boolQueryBuilder.filter(QueryBuilders.rangeQuery("time")
                .gte(LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))));*/

        /*boolQueryBuilder.filter(QueryBuilders.rangeQuery("pass_time")
                .gte(time));*/

//         boolQueryBuilder.filter(QueryBuilders.rangeQuery("pass_time")
//                .gte(parse.getTime()));


        searchRequest.source(builder.query(boolQueryBuilder).size(10));

        System.out.println("***********************");
        System.out.println(searchRequest);
        RestHighLevelClient connection = getConnection();
        SearchResponse search = connection.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("***********************");
        search.getHits().forEach(hit -> {
            hit.getSourceAsMap().forEach((k, v) -> {
                System.out.println(k + ":" + v);
            });
        });

    }

    @Test
    public void time() throws IOException, ParseException {
        String time = "2024-02-01T12:00:00.000Z";
        String format = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        System.out.println(format);
    }




    private RestHighLevelClient getConnection() {
        HttpHost[] httpHosts = Stream.of(url).map(ip -> {
            return new HttpHost(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
        }).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(httpHosts)
                .setRequestConfigCallback(config -> config.setConnectTimeout(5000 * 1000)
                        .setSocketTimeout(6000 * 1000));

        return new RestHighLevelClient(builder);
    }
}
