package com.boot.es.task;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * EsToKafkaByScroll
 * 将es的数据同步到kafka
 *
 * @author yuez
 * @since 2025/5/23
 */
public class EsToKafkaByScroll {
    interface EsConnectionConfig {
        String SCHEME = "https";
        String ES_USERNAME = "your_username";
        String ES_PASSWORD = "your_password";
        String ES_HOST = "your_host";
        int ES_PORT = 9205;
        int CONNECT_TIMEOUT = 5000 * 1000;
        int SOCKET_TIMEOUT = 6000 * 1000;
    }

    final static String ES_INDEX = "your_index";
    final static int ES_BATCH_SIZE = 8000;
    final static TimeValue SCROLL_TIMEOUT = TimeValue.timeValueMinutes(1);

    final static int BATCH_SEND_KAFKA = 100;

    interface  KafkaConnectionConfig {
        String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    }

    final static String KAFKA_TOPIC = "your_topic";

    public static void main(String[] args) {
        RestHighLevelClient esClient = createESClient();
        KafkaProducer<String, String> producer = createKafkaProducer();

        try {
            SearchRequest searchRequest = new SearchRequest(ES_INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(ES_BATCH_SIZE);
            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(SCROLL_TIMEOUT);

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            // 创建批量发送列表
            List<ProducerRecord<String, String>> batchRecords = new ArrayList<>();

            while (searchHits != null && searchHits.length > 0) {
                for (SearchHit hit : searchHits) {
                    String id = hit.getId();
                    String sourceAsString = hit.getSourceAsString();

                    // 添加到批处理列表
                    batchRecords.add(new ProducerRecord<>(KAFKA_TOPIC, id, sourceAsString));

                    // 当达到批处理大小时发送
                    if (batchRecords.size() >= BATCH_SEND_KAFKA) {
                        sendBatch(producer, batchRecords);
                        batchRecords.clear();
                    }
                }

                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(TimeValue.timeValueMinutes(1));
                searchResponse = esClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();
            }

            // 发送剩余的记录
            if (!batchRecords.isEmpty()) {
                sendBatch(producer, batchRecords);
            }

            // 清理 scroll
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            esClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                producer.close();
                esClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 批量发送方法
    private static void sendBatch(KafkaProducer<String, String> producer,
                                  List<ProducerRecord<String, String>> records) {
        List<Future<RecordMetadata>> futures = new ArrayList<>();

        // 异步发送所有记录
        for (ProducerRecord<String, String> record : records) {
            futures.add(producer.send(record));
        }

        // 等待所有发送完成
        for (Future<RecordMetadata> future : futures) {
            try {
                future.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static RestHighLevelClient createESClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(
                        EsConnectionConfig.ES_HOST,
                        EsConnectionConfig.ES_PORT,
                        EsConnectionConfig.SCHEME)
        );
        if (("https").equals(EsConnectionConfig.SCHEME)) {
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                try {
                    // 配置 SSL
                    SSLContext sslContext = SSLContexts.custom()
                            .loadTrustMaterial(null, (x509Certificates, s) -> true)
                            .build();

                    httpClientBuilder.setSSLContext(sslContext);

                    // 配置认证
                    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(
                                    EsConnectionConfig.ES_USERNAME,
                                    EsConnectionConfig.ES_PASSWORD));
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

                    return httpClientBuilder;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                return builder
                        .setConnectTimeout(EsConnectionConfig.CONNECT_TIMEOUT)//连接超时（默认1秒）
                        .setSocketTimeout(EsConnectionConfig.SOCKET_TIMEOUT);// 套接字超时（默认为30秒）//更改客户端的超时限制默认30秒现在改为100*1000分钟
            }
        });
        return new RestHighLevelClient(builder);
    }

    private static KafkaProducer<String, String> createKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaConnectionConfig.KAFKA_BOOTSTRAP_SERVERS);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }
}
