package com.boot.es.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by zy on 2019/7/31.
 */
@ApiModel(value = "分页")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageVo {
    @JsonIgnore
    @ApiModelProperty(value="当前页",name="page")
    private Integer page;
    @JsonIgnore
    @ApiModelProperty(value="当前数量",name="limit")
    private Integer limit;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer from;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer to;

    public Integer getFrom() {
        if(page!=null && limit!=null){
            if((page-1)*limit >10000){
                int i = (page-1)*limit/10000;
                return (page-1)*limit-10000*i;
            }
            return (page-1)*limit;
        }
        return 0;
    }

    public Integer getLimit(){
        if(limit!=null&&getFrom()+limit>=10000){
            return 10000-getFrom();
        }
        return limit;
    }

    public Integer getTo() {
        if(page!=null && limit!=null){
            return page*limit;
        }
        return 0;
    }





}
