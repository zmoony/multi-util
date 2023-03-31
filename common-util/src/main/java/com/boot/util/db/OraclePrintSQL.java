package com.boot.util.db;

import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OraclePrintSQL
 * mysql可以直接使用assql()
 * @author yuez
 * @since 2023/3/31
 */
@Log4j2
public class OraclePrintSQL {
    /**
     * 在开发过程，SQL语句有可能写错，如果能把运行时出错的 SQL 语句直接打印出来，那对排错非常方便，因为其可以直接拷贝到数据库客户端进行调试。
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 实际 sql 语句
     */
    public static String printRealSql(String sql, Object[] params) {
        if (params == null || params.length == 0) {
            log.info("The SQL is------------>\n" + sql);
            return sql;
        }

        if (!match(sql, params)) {
            log.info("SQL 语句中的占位符与参数个数不匹配。SQL：" + sql);
            return null;
        }

        int cols = params.length;
        Object[] values = new Object[cols];
        System.arraycopy(params, 0, values, 0, cols);

        for (int i = 0; i < cols; i++) {
            Object value = values[i];
            if (value instanceof Date) {
                values[i] = "'" + value + "'";
            } else if (value instanceof String) {
                values[i] = "'" + value + "'";
            } else if (value instanceof Boolean) {
                values[i] = (Boolean) value ? 1 : 0;
            }
        }

        String statement = String.format(sql.replaceAll("\\?", "%s"), values);

        log.info("The SQL is------------>\n" + statement);

        return statement;
    }

    /**
     * ? 和参数的实际个数是否匹配
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return true 表示为 ? 和参数的实际个数匹配
     */
    private static boolean match(String sql, Object[] params) {
        if (params == null || params.length == 0) return true; // 没有参数，完整输出

        Matcher m = Pattern.compile("(\\?)").matcher(sql);
        int count = 0;
        while (m.find()) {
            count++;
        }

        return count == params.length;
    }

}
