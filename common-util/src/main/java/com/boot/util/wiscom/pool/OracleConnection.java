/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom.pool;


import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 获取数据库连接
 * @author zhouxingxing
 */
@Log4j2
public class OracleConnection {
    
    public Connection getConnection(){
        Connection conn = null;

        if (Boolean.valueOf(GlobalObject.properties_business_my.getProperty("druid.flag"))) {
            //使用druid连接池
            conn = DruidConnectionPool.getConnection();
            return conn;
        }
        StringBuilder url=new StringBuilder("jdbc:oracle:thin:@");
        url.append(GlobalObject.properties_business_my.getProperty("oracle.host").trim());
        url.append(":");
        url.append(GlobalObject.properties_business_my.getProperty("oracle.port").trim());
        url.append("/");
        url.append(GlobalObject.properties_business_my.getProperty("oracle.server").trim());
        
        OracleDataSource ods;
        try {                       
            ods = new OracleDataSource();
            ods.setURL(url.toString());
            ods.setUser(GlobalObject.properties_business_my.getProperty("oracle.user").trim());
            ods.setPassword(GlobalObject.properties_business_my.getProperty("oracle.password").trim());
            try {
                    conn=ods.getConnection();
                } catch (SQLException e) {
                    log.error("源库连接信息错误，无法获取Connection"+e.getMessage());
                }
        } catch (SQLException e) {
                log.error("缺少数据库JDBC驱动包！");

        }
        return conn;
    }

    public Connection getSourceConnection() {
        Connection conn = null;

        if (Boolean.valueOf(GlobalObject.properties_business_my.getProperty("source.druid.flag"))) {
            //使用druid连接池
            conn = SourceDruidConnectionPool.getConnection();
            return conn;
        }
        StringBuilder url=new StringBuilder("jdbc:oracle:thin:@");
        url.append(GlobalObject.properties_business_my.getProperty("source.oracle.host").trim());
        url.append(":");
        url.append(GlobalObject.properties_business_my.getProperty("source.oracle.port").trim());
        url.append("/");
        url.append(GlobalObject.properties_business_my.getProperty("source.oracle.server").trim());

        OracleDataSource ods;
        try {
            ods = new OracleDataSource();
            ods.setURL(url.toString());
            ods.setUser(GlobalObject.properties_business_my.getProperty("source.oracle.user").trim());
            ods.setPassword(GlobalObject.properties_business_my.getProperty("source.oracle.password").trim());
            try {
                conn=ods.getConnection();
            } catch (SQLException e) {
                log.error("源库连接信息错误，无法获取Connection"+e.getMessage());
            }
        } catch (SQLException e) {
            log.error("缺少数据库JDBC驱动包！");

        }
        return conn;
    }


  
    
}