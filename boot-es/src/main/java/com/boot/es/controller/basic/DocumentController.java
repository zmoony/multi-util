package com.boot.es.controller.basic;

import com.boot.es.annotation.LogDescribe;
import com.boot.es.common.R;
import com.boot.es.service.DocumentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * 文档操作集合
 *
 * @author yuez
 * @version 1.0.0
 * @className DocumentController
 * @date 2021/3/15 16:59
 **/

@Api(value = "文档操作集合",tags = "文档操作集合")
@RestController
@RequestMapping("doc")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "增加文档", notes = "路径传参")
    @PostMapping("/add/{index}/{id}")
    public R addDocument(@PathVariable String index, @PathVariable String id, @RequestBody String bodyJson) throws IOException {
        return documentService.addDocument(index, id, bodyJson);
    }

    @ApiOperation(value = "是否存在文档",notes = "路径传参")
    @GetMapping("/exist/{index}/{type}/{id}")
    public R existDocument(@PathVariable String index, @PathVariable String type, @PathVariable String id) throws IOException {
        return documentService.existDocument(index, type, id);
    }

    @ApiOperation(value = "获取文档",notes = "路径传参")
    @GetMapping("/get/{index}/{type}/{id}")
    public R getDocument(@PathVariable String index, @PathVariable String type, @PathVariable String id) throws IOException {
        return documentService.getDocument(index, type, id);
    }

    @ApiOperation(value = "更新文档", notes = "路径传参")
    @PostMapping("/add/{index}/{type}/{id}")
    public R updateDocument(@PathVariable String index, @PathVariable String type, @PathVariable String id, @RequestBody String bodyJson) throws IOException {
        return documentService.updateDocument(index, type, id, bodyJson);
    }

    @ApiOperation(value = "删除文档", notes = "路径传参")
    @DeleteMapping("/delete/{index}/{type}/{id}")
    public R deleteDocument(@PathVariable String index, @PathVariable String type, @PathVariable String id) throws IOException {
        return documentService.deleteDocument(index, type, id);
    }


    @ApiOperation(value = "批量新增文档", notes = "路径传参,消息体示例：{\n" +
            "    \"1\":\"{\\\"object_id\\\":13500000606468,\\\"basic_id\\\":null,\\\"info_kind\\\":1,\\\"origin_picture_id\\\":\\\" \\\",\\\"tollgate_id\\\":\\\"3204815291\\\",\\\"ape_id\\\":\\\"\\\",\\\"file_uri\\\":\\\"/pic?=d49ie25e*98fi88d-776609220=02i0m*ep1t2i0d1=*ipd6=*9s4=33b5ifdb1*a8fb4eddb-06c3010-1627s6-f1z993d0=b2\\\",\\\"close_uri\\\":\\\" \\\",\\\"plate_uri\\\":\\\"/pic?=d2eief7i99cf*76d-782c09-6cd04b68ab1befifb3*=9d9*51d2i*s1d=i6p7t=pe*m0i20=03202-161076-c7s=a3d4zb3\\\",\\\"prospect_uri\\\":\\\" \\\",\\\"compose_uri\\\":\\\" \\\",\\\"thumb_uri\\\":\\\" \\\",\\\"left_topx\\\":-1,\\\"left_topy\\\":-1,\\\"right_btmx\\\":-1,\\\"right_btmy\\\":-1,\\\"mark_time\\\":null,\\\"lane_no\\\":5,\\\"has_plate\\\":1,\\\"plate_class\\\":\\\"02\\\",\\\"plate_color\\\":\\\"1\\\",\\\"plate_no\\\":\\\"苏D1VH98\\\",\\\"attach_no\\\":\\\" \\\",\\\"plate_describe\\\":\\\" \\\",\\\"is_decked\\\":-1,\\\"is_altered\\\":-1,\\\"is_covered\\\":-1,\\\"speed\\\":0,\\\"direction\\\":3,\\\"driving_status\\\":-1,\\\"using_kind\\\":\\\" \\\",\\\"vehicle_class\\\":\\\"9\\\",\\\"vehicle_brand\\\":-1,\\\"vehicle_model\\\":-1,\\\"vehicle_style\\\":-1,\\\"vehicle_color\\\":\\\" \\\",\\\"pass_time\\\":\\\"20200602162847\\\",\\\"road_name\\\":\\\" \\\",\\\"has_annual\\\":-1,\\\"annual_num\\\":-1,\\\"has_tissuebox\\\":-1,\\\"has_ornaments\\\":-1,\\\"sunvisor_status\\\":-1,\\\"safetybelt_status\\\":-1,\\\"calling_status\\\":-1,\\\"vehicle_bigclass\\\":\\\" \\\",\\\"feature_uri\\\":\\\" \\\",\\\"analy_state\\\":0,\\\"report_time\\\":\\\"20200602162807\\\",\\\"orientation\\\":1,\\\"flow_statistic\\\":1,\\\"server_id\\\":\\\"172.21.188.196\\\",\\\"face_id\\\":\\\"\\\",\\\"src_ip\\\":\\\"192.18.3.232_6\\\"}\",\n" +
            "    \"2\":\"{\\\"object_id\\\":13500000606468,\\\"basic_id\\\":null,\\\"info_kind\\\":1,\\\"origin_picture_id\\\":\\\" \\\",\\\"tollgate_id\\\":\\\"3204815291\\\",\\\"ape_id\\\":\\\"\\\",\\\"file_uri\\\":\\\"/pic?=d49ie25e*98fi88d-776609220=02i0m*ep1t2i0d1=*ipd6=*9s4=33b5ifdb1*a8fb4eddb-06c3010-1627s6-f1z993d0=b2\\\",\\\"close_uri\\\":\\\" \\\",\\\"plate_uri\\\":\\\"/pic?=d2eief7i99cf*76d-782c09-6cd04b68ab1befifb3*=9d9*51d2i*s1d=i6p7t=pe*m0i20=03202-161076-c7s=a3d4zb3\\\",\\\"prospect_uri\\\":\\\" \\\",\\\"compose_uri\\\":\\\" \\\",\\\"thumb_uri\\\":\\\" \\\",\\\"left_topx\\\":-1,\\\"left_topy\\\":-1,\\\"right_btmx\\\":-1,\\\"right_btmy\\\":-1,\\\"mark_time\\\":null,\\\"lane_no\\\":5,\\\"has_plate\\\":1,\\\"plate_class\\\":\\\"02\\\",\\\"plate_color\\\":\\\"1\\\",\\\"plate_no\\\":\\\"苏D1VH98\\\",\\\"attach_no\\\":\\\" \\\",\\\"plate_describe\\\":\\\" \\\",\\\"is_decked\\\":-1,\\\"is_altered\\\":-1,\\\"is_covered\\\":-1,\\\"speed\\\":0,\\\"direction\\\":3,\\\"driving_status\\\":-1,\\\"using_kind\\\":\\\" \\\",\\\"vehicle_class\\\":\\\"9\\\",\\\"vehicle_brand\\\":-1,\\\"vehicle_model\\\":-1,\\\"vehicle_style\\\":-1,\\\"vehicle_color\\\":\\\" \\\",\\\"pass_time\\\":\\\"20200602162847\\\",\\\"road_name\\\":\\\" \\\",\\\"has_annual\\\":-1,\\\"annual_num\\\":-1,\\\"has_tissuebox\\\":-1,\\\"has_ornaments\\\":-1,\\\"sunvisor_status\\\":-1,\\\"safetybelt_status\\\":-1,\\\"calling_status\\\":-1,\\\"vehicle_bigclass\\\":\\\" \\\",\\\"feature_uri\\\":\\\" \\\",\\\"analy_state\\\":0,\\\"report_time\\\":\\\"20200602162807\\\",\\\"orientation\\\":1,\\\"flow_statistic\\\":1,\\\"server_id\\\":\\\"172.21.188.196\\\",\\\"face_id\\\":\\\"\\\",\\\"src_ip\\\":\\\"192.18.3.232_6\\\"}\"\n" +
            "}")
    @ApiImplicitParam(name = "map",value = "key为id,value为文档")
    @PostMapping("/bulkadd/{index}/{type}")
    public R bulkDocument(@PathVariable String index, @PathVariable String type,
                          @RequestBody  Map<String, String> map) throws IOException {
        return documentService.bulkDocument(index, type, map);
    }

    @LogDescribe
    @ApiOperation(value = "获取文档数",notes = "路径传参")
    @GetMapping("/size/{index}")
    public R documentSize(@PathVariable String index) throws IOException {
        return documentService.documentSize(index);
    }


}
