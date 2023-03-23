package com.boot.es.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * index的操作类
 *
 * @author yuez
 * @version 1.0.0
 * @className IndexVo
 * @date 2021/3/15 11:03
 **/
@ApiModel(value = "index的操作类")
@Data
public class IndexVo {
    @ApiModelProperty(value = "索引",required = true)
    private String index;
    @ApiModelProperty(value = "类型，默认_doc",example = "_doc")
    private String type = "_doc";
    @ApiModelProperty(value = "分片数，默认1",example = "1")
    private Integer numberOfShards = 1;
    @ApiModelProperty(value = "副本数，默认0",example = "0")
    private Integer numberOfReplicas = 0;
    @ApiModelProperty(value = "mapping结构",required = true,example = "{    \"properties\": {        \"report_time\": {            \"type\": \"date\"        },        \"trigger_time\": {            \"type\": \"date\"        },        \"upload_time\": {            \"type\": \"date\"        },        \"tollgate_id\": {            \"type\": \"keyword\",            \"eager_global_ordinals\": true        },        \"src_ip\": {            \"type\": \"keyword\"        }    }}")
    private String propertiesJson;
}
