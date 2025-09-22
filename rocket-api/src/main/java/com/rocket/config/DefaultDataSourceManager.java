package com.rocket.config;

import com.github.alenfive.rocketapi.datasource.DataSourceDialect;
import com.github.alenfive.rocketapi.datasource.DataSourceManager;
import com.github.alenfive.rocketapi.datasource.MySQLDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * DefaultDataSourceManager
 *
 * @author yuez
 * @since 2024/1/11
 */
@Component
public class DefaultDataSourceManager extends DataSourceManager {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        Map<String, DataSourceDialect> dialects = new HashMap<>();
        //不同的数据库使用不同的数据源，这里使用 `MySQLDataSource`
        dialects.put("mysql", new MySQLDataSource(dataSource,true));
        super.setDialectMap(dialects);
    }
}
