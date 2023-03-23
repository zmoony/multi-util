package com.boot.es.dao;

import java.io.IOException;
import java.util.List;

/**
 * index的删除与增加
 *
 * @author yuez
 * @version 1.0.0
 * @className IndexDao
 * @date 2021/3/13 13:40
 **/
public interface IndexDao {
    /**
     * 　　 删除索引
     * 　　 @param index 索引
     * 　　 @return boolean
     * 　　 @throws java.io.IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 13:45
     */
    boolean delIndex(String index) throws IOException;

    /**
     * 　　增加索引
     * 　　 @param index  索引
     * 　　 @param type  类型，7.x统一为_doc
     * 　　 @param numberOfShards 分片数
     * 　　 @param numberOfReplicas 副本数
     * 　　 @param propertiesJson 结构体
     * 　　 @return boolean
     * 　　 @throws java.io.IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 13:45
     */
    boolean addIndex(String index, String type, Integer numberOfShards, Integer numberOfReplicas, String propertiesJson) throws IOException;

    /**
     * 　　判断索引是否存在
     * 　　 @param index 索引
     * 　　 @return boolean
     * 　　 @throws java.io.IOException
     * 　　 @author yuez
     * 　　 @date 2021/3/13 14:07
     */
    boolean existIndex(String index) throws IOException;

}
