package com.boot.es.test;

import com.boot.es.dao.DocumentDao;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.core.CountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 测试批量插入index
 *
 * @author yuez
 * @version 1.0.0
 * @className BulkTest
 * @date 2021/3/18 9:27
 **/
@Component
public class BulkTest2 {
    private static String index = "index_vehicle_y";
    private static String type = "_doc";
    private static String body = "{\n" +
            "    \"object_id\": 13500000606468,\n" +
            "    \"basic_id\": null,\n" +
            "    \"info_kind\": 1,\n" +
            "    \"origin_picture_id\": \" \",\n" +
            "    \"tollgate_id\": \"3204815291\",\n" +
            "    \"ape_id\": \"\",\n" +
            "    \"left_topx\": -1,\n" +
            "    \"left_topy\": -1,\n" +
            "    \"right_btmx\": -1,\n" +
            "    \"right_btmy\": -1,\n" +
            "    \"mark_time\": null,\n" +
            "    \"lane_no\": 5,\n" +
            "    \"has_plate\": 1,\n" +
            "    \"plate_class\": \"02\",\n" +
            "    \"plate_color\": \"1\",\n" +
            "    \"plate_no\": \"苏D1VH98\",\n" +
            "    \"attach_no\": \" \",\n" +
            "    \"plate_describe\": \" \",\n" +
            "    \"is_decked\": -1,\n" +
            "    \"is_altered\": -1,\n" +
            "    \"is_covered\": -1,\n" +
            "    \"speed\": 0,\n" +
            "    \"direction\": 3,\n" +
            "    \"driving_status\": -1,\n" +
            "    \"using_kind\": \" \",\n" +
            "    \"vehicle_class\": \"9\",\n" +
            "    \"vehicle_brand\": -1,\n" +
            "    \"vehicle_model\": -1,\n" +
            "    \"vehicle_style\": -1,\n" +
            "    \"vehicle_color\": \" \",\n" +
            "    \"pass_time\": \"20200602162847\",\n" +
            "    \"road_name\": \" \",\n" +
            "    \"has_annual\": -1,\n" +
            "    \"annual_num\": -1,\n" +
            "    \"has_tissuebox\": -1,\n" +
            "    \"has_ornaments\": -1,\n" +
            "    \"sunvisor_status\": -1,\n" +
            "    \"safetybelt_status\": -1,\n" +
            "    \"calling_status\": -1,\n" +
            "    \"vehicle_bigclass\": \" \",\n" +
            "    \"analy_state\": 0,\n" +
            "    \"report_time\": \"20200602162807\",\n" +
            "    \"orientation\": 1,\n" +
            "    \"flow_statistic\": 1,\n" +
            "    \"server_id\": \"172.21.188.196\",\n" +
            "    \"face_id\": \"\",\n" +
            "    \"src_ip\": \"192.18.3.232_6\"\n" +
            "}";
    private volatile static int count = 10000000-9982471;
    private volatile static int perCount = 1000;
    private volatile static int forCount = count / perCount;

    @Autowired
    private DocumentDao documentDao;

    public void bulk() throws IOException {
        bulkNoUri();
    }

    public void bulk2() throws IOException {
        bulkNoUri();
    }

    public void bulk3() throws IOException {
        bulkNoUri(5000);
    }


    private void bulkNoUri(int... perCount) throws IOException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("es-bulk-pool-%d").build();
        ExecutorService service = new ThreadPoolExecutor(600, 1000,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2048), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        System.out.println("********开始******");
        CountResponse countResponse = documentDao.documentSize(index);
        System.out.println(countResponse);
        count = count - (int)countResponse.getCount();
        if(perCount.length == 0){
            perCount = new int[]{BulkTest2.perCount};
        }
        forCount = count / perCount[0];
//        ExecutorService service = Executors.newFixedThreadPool(200);
        for (int a = 0; a < forCount; a++) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < perCount[0]; i++) {
                map.put(UUID.randomUUID().toString().replaceAll("_", ""), body);
            }
            service.execute(
                    () -> {
                        BulkResponse bulkItemResponses = null;
                        try {
                            bulkItemResponses = documentDao.bulkDocument(index, type, map);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(bulkItemResponses.hasFailures() + ":" + map.size());
                    });
        }
        service.shutdown();
        System.out.println("********结束******");
        System.out.println(documentDao.documentSize(index));
    }
}
