package com.boot.util.wiscom.pool;

import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;

@Log4j2
/**
 * PostgreSQL数据库连接
 * @author hwang
 * @date 2022-04-02
 */
public class PostgreSQLConnection {
    public Connection getConnection() {
        Connection conn = null;
        if (Boolean.valueOf(GlobalObject.properties_business_my.getProperty("druid.flag"))) {
            //使用druid连接池
            conn = DruidConnectionPool.getConnection();
            return conn;
        }
        StringBuilder url = new StringBuilder("jdbc:postgresql://");
        url.append(GlobalObject.properties_business_my.getProperty("postgreSQL.host").trim());
        url.append(":");
        url.append(GlobalObject.properties_business_my.getProperty("postgreSQL.port").trim());
        url.append("/");
        url.append(GlobalObject.properties_business_my.getProperty("postgreSQL.db").trim());
        url.append("?currentSchema=");
        url.append(GlobalObject.properties_business_my.getProperty("postgreSQL.schema").trim());

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url.toString(),
                    GlobalObject.properties_business_my.getProperty("postgreSQL.user").trim(),
                    GlobalObject.properties_business_my.getProperty("postgreSQL.password").trim());
        } catch (Exception e) {
            log.error("PostgreSQL获取连接失败！原因:{}", e.getMessage());
        }
        return conn;
    }
}
