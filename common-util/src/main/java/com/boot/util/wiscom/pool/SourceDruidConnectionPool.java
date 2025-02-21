package com.boot.util.wiscom.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * druid 源数据库连接池
 *
 * @author hwang
 */
@SuppressWarnings("all")
@Log4j2
public class SourceDruidConnectionPool {
    private static String DRUID_URL = GlobalObject.properties_business_my.getProperty("source.druid.url").trim();
    private static String DRUID_USER = GlobalObject.properties_business_my.getProperty("source.druid.user").trim();
    private static String DRUID_PASSWORD = GlobalObject.properties_business_my.getProperty("source.druid.password").trim();
    private static int DRUID_INIT_SIZE = Integer.valueOf(GlobalObject.properties_business_my.getProperty("source.druid.initSize").trim());
    private static int DRUID_MAX_SIZE = Integer.valueOf(GlobalObject.properties_business_my.getProperty("source.druid.maxSize").trim());
    private static int DRUID_MIN_IDLE = Integer.valueOf(GlobalObject.properties_business_my.getProperty("source.druid.minSize").trim());
    private static int DRUID_MAX_WAIT = Integer.valueOf(GlobalObject.properties_business_my.getProperty("source.druid.maxWait").trim());
    private static int DRUID_MAX_OPS = Integer.valueOf(GlobalObject.properties_business_my.getProperty("source.druid.maxOpenPrepareStatements").trim());

    private static DruidDataSource dataSource;

    static  {
        dataSource = new DruidDataSource();
        dataSource.setUrl(DRUID_URL);
        dataSource.setUsername(DRUID_USER);
        dataSource.setPassword(DRUID_PASSWORD);
        //初始连接数，默认0
        dataSource.setInitialSize(DRUID_INIT_SIZE);
        //最大连接数，默认8
        dataSource.setMaxActive(DRUID_MAX_SIZE);
        //最小闲置数
        dataSource.setMinIdle(DRUID_MIN_IDLE);
        //获取连接的最大等待时间，单位毫秒
        dataSource.setMaxWait(DRUID_MAX_WAIT);
        //缓存PreparedStatement，默认false
        dataSource.setPoolPreparedStatements(true);
        //缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码
        dataSource.setMaxOpenPreparedStatements(DRUID_MAX_OPS);
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException ex) {
            log.error("获取数据库连接失败，原因:{}",ex.getMessage());
        }
        return conn;
    }
}
