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
//@Component
@SpringBootTest
public class BulkTest {
//    private static String index = "index_vehicle_y";
    private static String index = "index_vehicle_y3";
    private static String type = "_doc";
//    private static String body = "{\"object_id\":13500000606468,\"basic_id\":null,\"info_kind\":1,\"origin_picture_id\":\" \",\"tollgate_id\":\"3204815291\",\"ape_id\":\"\",\"left_topx\":-1,\"left_topy\":-1,\"right_btmx\":-1,\"right_btmy\":-1,\"mark_time\":null,\"lane_no\":5,\"has_plate\":1,\"plate_class\":\"02\",\"plate_color\":\"1\",\"plate_no\":\"苏D1VH98\",\"attach_no\":\" \",\"plate_describe\":\" \",\"is_decked\":-1,\"is_altered\":-1,\"is_covered\":-1,\"speed\":0,\"direction\":3,\"driving_status\":-1,\"using_kind\":\" \",\"vehicle_class\":\"9\",\"vehicle_brand\":-1,\"vehicle_model\":-1,\"vehicle_style\":-1,\"vehicle_color\":\" \",\"pass_time\":\"20200602162847\",\"road_name\":\" \",\"has_annual\":-1,\"annual_num\":-1,\"has_tissuebox\":-1,\"has_ornaments\":-1,\"sunvisor_status\":-1,\"safetybelt_status\":-1,\"calling_status\":-1,\"vehicle_bigclass\":\" \",\"analy_state\":0,\"report_time\":\"20200602162807\",\"orientation\":1,\"flow_statistic\":1,\"server_id\":\"172.21.188.196\",\"face_id\":\"\",\"src_ip\":\"192.18.3.232_6\"}";
    private static String body = "{\"object_id\":13500000606468,\"basic_id\":null,\"info_kind\":1,\"origin_picture_id\":\" \",\"tollgate_id\":\"3204815291\",\"ape_id\":\"\",\"file_uri\":\"/pic?=d49ie25e*98fi88d-776609220=02i0m*ep1t2i0d1=*ipd6=*9s4=33b5ifdb1*a8fb4eddb-06c3010-1627s6-f1z993d0=b2\",\"close_uri\":\" \",\"plate_uri\":\"/pic?=d2eief7i99cf*76d-782c09-6cd04b68ab1befifb3*=9d9*51d2i*s1d=i6p7t=pe*m0i20=03202-161076-c7s=a3d4zb3\",\"prospect_uri\":\" \",\"compose_uri\":\" \",\"thumb_uri\":\" \",\"left_topx\":-1,\"left_topy\":-1,\"right_btmx\":-1,\"right_btmy\":-1,\"mark_time\":null,\"lane_no\":5,\"has_plate\":1,\"plate_class\":\"02\",\"plate_color\":\"1\",\"plate_no\":\"苏D1VH98\",\"attach_no\":\" \",\"plate_describe\":\" \",\"is_decked\":-1,\"is_altered\":-1,\"is_covered\":-1,\"speed\":0,\"direction\":3,\"driving_status\":-1,\"using_kind\":\" \",\"vehicle_class\":\"9\",\"vehicle_brand\":-1,\"vehicle_model\":-1,\"vehicle_style\":-1,\"vehicle_color\":\" \",\"pass_time\":\"20200602162847\",\"road_name\":\" \",\"has_annual\":-1,\"annual_num\":-1,\"has_tissuebox\":-1,\"has_ornaments\":-1,\"sunvisor_status\":-1,\"safetybelt_status\":-1,\"calling_status\":-1,\"vehicle_bigclass\":\" \",\"feature_uri\":\" \",\"analy_state\":0,\"report_time\":\"20200602162807\",\"orientation\":1,\"flow_statistic\":1,\"server_id\":\"172.21.188.196\",\"face_id\":\"\",\"src_ip\":\"192.18.3.232_6\"}";

    private volatile static int count = 4000000;
    private volatile static int perCount = 1000;
    private volatile static int forCount = count / perCount;

    @Autowired
    private DocumentDao documentDao;

    @Test
    public void bulk() throws IOException {
        bulkNoUri();
    }

    @Test
    public void bulk2() throws IOException {
        bulkNoUri(1000);
    }

    @Test
    public void bulk3() throws IOException {
        bulkNoUri();
    }


    private void bulkNoUri(int... perCount) throws IOException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("es-bulk-pool-%d").build();
        ExecutorService service = new ThreadPoolExecutor(60, 100,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2048), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        System.out.println("********开始******");
        CountResponse countResponse = documentDao.documentSize(index);
        System.out.println(countResponse);
        count = count - (int)countResponse.getCount();
        if(count <=0 ){
            System.out.println("到达0");
            return;
        }
        if(perCount.length == 0){
            perCount = new int[]{BulkTest.perCount};
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
