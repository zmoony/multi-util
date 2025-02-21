package com.boot.util.wiscom.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * druid 目标数据库连接池
 *
 *
 * 如果不调用从Druid连接池中获取的Connection对象的close()方法，或没有通过try-with-resources等机制确保其在使用完毕后被正确关闭和归还给连接池，
 * 可能会导致以下问题：
 * 1. **资源泄露**：数据库连接是一种宝贵的系统资源，每个连接都会占用一定的内存和其他系统资源。若不及时归还到连接池，
 * 长时间累积下来会导致系统资源耗尽，影响系统的稳定性和性能。
 *
 * 2. **连接数超出限制**：如果程序持续不断地获取连接但不释放，最终会使得连接池中的活动连接数达到上限，后续请求将无法获取新的连接，
 * 从而可能导致应用出现无法处理数据库操作的情况。
 *
 * 3. **死锁和超时**：未关闭的连接可能持有事务锁定或其他资源，这可能会导致其他并发事务等待这些资源释放，进而引发死锁或者事务超时等问题。
 *
 * 4. **数据库服务器压力增大**：过多的闲置连接也会对数据库服务器造成额外的压力，因为它需要为这些连接维持状态并进行管理。
 *
 * 因此，在实际开发过程中，必须确保从Druid连接池获取的连接在使用完毕后能够正确、及时地归还到连接池，
 * 通常的做法就是在一个合适的finally块或try-with-resources语句中调用Connection.close()方法。
 * Druid连接池内部会对这个close操作进行处理，将其归还至连接池而不是真正关闭连接。
 *
 * @author hwang
 */
@SuppressWarnings("all")
@Log4j2
public class DruidConnectionPool {
    private static String DRUID_URL = GlobalObject.properties_business_my.getProperty("druid.url").trim();
    private static String DRUID_USER = GlobalObject.properties_business_my.getProperty("druid.user").trim();
    private static String DRUID_PASSWORD = GlobalObject.properties_business_my.getProperty("druid.password").trim();
    private static int DRUID_INIT_SIZE = Integer.valueOf(GlobalObject.properties_business_my.getProperty("druid.initSize").trim());
    private static int DRUID_MAX_SIZE = Integer.valueOf(GlobalObject.properties_business_my.getProperty("druid.maxSize").trim());
    private static int DRUID_MIN_IDLE = Integer.valueOf(GlobalObject.properties_business_my.getProperty("druid.minSize").trim());
    private static int DRUID_MAX_WAIT = Integer.valueOf(GlobalObject.properties_business_my.getProperty("druid.maxWait").trim());
    private static int DRUID_MAX_OPS = Integer.valueOf(GlobalObject.properties_business_my.getProperty("druid.maxOpenPrepareStatements").trim());

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

    /**
     * DataSource dataSource = // 初始化Druid数据源
     *  代码块执行完后自动调用close方法，并且Druid会将连接归还到连接池。
     * try (Connection conn = dataSource.getConnection()) {
     *     // 执行SQL操作
     * } catch (SQLException e) {
     *     // 处理异常
     * }
     * 【TIP】真正的关闭是用conn.abort(){不建议}，conn.close()只是将连接归还到连接池，不会关闭连接。
     * @return
     */
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
