package com.boot.es.service;

import com.boot.es.vo.IndexVo;

import java.io.IOException;

/**
 * 索引的后续处理
 *
 * @author yuez
 * @version 1.0.0
 * @className IndexService
 * @date 2021/3/15 10:19
 **/
public interface IndexService {
    boolean delIndex(String index) throws IOException;
    boolean addIndex(IndexVo vo) throws IOException;
    boolean existIndex(String index) throws IOException;
}
