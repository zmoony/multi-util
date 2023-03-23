package com.boot.es.dao;

import com.boot.es.exception.GlobalException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.search.aggregations.bucket.range.GeoDistanceAggregationBuilder;

import java.io.IOException;
import java.util.List;

/**
 * 地图的操作类
 *
 * @author yuez
 * @version 1.0.0
 * @className GeoDao
 * @date 2021/3/15 9:18
 **/
public interface GeoDao {
    /**
     * 　　两点确定区域搜索
     * 　　 @param index 索引
     * 　　 @param points 地图点位，容量为两点
     * 　　 @return org.elasticsearch.action.search.SearchResponse
     * 　　 @throws IOException, GlobalException
     * 　　 @author yuez
     * 　　 @date 2021/3/15 9:42
     */
    SearchResponse geoBoundingBox(String index, List<GeoPoint> points) throws IOException, GlobalException;

    /**
     * 　　搜索(多点，多边形)
     * 　　 @param index 索引
     * 　　 @param points 地图点位，不得小于两点
     * 　　 @return org.elasticsearch.action.search.SearchResponse
     * 　　 @throws IOException, GlobalException
     * 　　 @author yuez
     * 　　 @date 2021/3/15 9:56
     */
    SearchResponse geoPolygon(String index, List<GeoPoint> points) throws IOException, GlobalException;

    /**
     * 　　搜索(根据距当前位置的距离)
     * 　　 @param index 索引
     * 　　 @param point 参照点
     * 　　 @param distance 距离，单位KM
     * 　　 @return org.elasticsearch.action.search.SearchResponse
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/15 10:01
     *
     */
    SearchResponse geoDistance(String index, GeoPoint point, String distance) throws IOException;

    /**
    　　聚合分析，（距离当前位置一定范围内有多少个酒店）
    　　 @param index 索引
    　　 @param point 参照点
    　　 @param ranges 范围集合
    　　 @return org.elasticsearch.action.search.SearchResponse
    　　 @throws 
    　　 @author yuez
    　　 @date 2021/3/15 10:15
    　　*/
    SearchResponse geoDistanceAgg(String index, GeoPoint point, List<GeoDistanceAggregationBuilder.Range> ranges) throws IOException;
}
