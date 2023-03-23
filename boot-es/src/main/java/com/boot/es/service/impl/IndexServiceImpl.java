package com.boot.es.service.impl;

import com.boot.es.dao.IndexDao;
import com.boot.es.service.IndexService;
import com.boot.es.vo.IndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 索引操作
 *
 * @author yuez
 * @version 1.0.0
 * @className IndexServiceImpl
 * @date 2021/3/15 10:22
 **/
@Service("indexService")
public class IndexServiceImpl implements IndexService {
    @Autowired
    private IndexDao indexDao;

    @Override
    public boolean delIndex(String index) throws IOException {
        return indexDao.delIndex(index);
    }

    @Override
    public boolean addIndex(IndexVo vo) throws IOException {
        return indexDao.addIndex(vo.getIndex(),vo.getType(),vo.getNumberOfShards(),vo.getNumberOfReplicas(),vo.getPropertiesJson());
    }

    @Override
    public boolean existIndex(String index) throws IOException {
        return indexDao.existIndex(index);
    }
}
