package com.boot.es.controller.basic;

import com.boot.es.common.R;
import com.boot.es.service.IndexService;
import com.boot.es.vo.IndexVo;
import com.boot.util.annotation.LogDescribe;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * index操作集合
 *
 * @author yuez
 * @version 1.0.0
 * @className IndexController
 * @date 2021/3/15 11:21
 **/
@Api(value = "index操作集合",tags = "索引操作集合")
@LogDescribe
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private IndexService indexService;

    @ApiOperation(value = "添加索引", nickname = "添加索引", notes = "入参是简单对象", produces = "application/json")
    @PostMapping
    public R addIndex(@RequestBody IndexVo vo) throws IOException {
        return indexService.addIndex(vo)?R.ok().message("添加索引成功"):R.error().message("添加索引失败");
    }

    @ApiOperation(value = "删除索引", nickname = "删除索引", notes = "路径传参")
    @DeleteMapping("/{index}")
    public R getIndex(@PathVariable("index") String index) throws IOException {
        return indexService.delIndex(index)?R.ok().message("删除成功"):R.error().message("删除失败");
    }

    @ApiOperation(value = "是否存在索引", nickname = "是否存在索引", notes = "路径传参")
    @GetMapping("exist/{index}")
    public R existIndex(@PathVariable("index") String index) throws IOException {
        return indexService.existIndex(index)?R.ok().message("存在索引"):R.ok().message("索引不存在");
    }

}
