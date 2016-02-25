package com.selonj.supports;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

import java.util.Properties;

/**
 * Created by L.x on 16-2-25.
 */
public class MySQLXADataSource {
    public static PoolingDataSource of(String name, final String databaseName) {
        PoolingDataSource dataSource = new PoolingDataSource();
        dataSource.setUniqueName(name);
        dataSource.setClassName(MysqlXADataSource.class.getName());
        dataSource.setMaxPoolSize(10);
        dataSource.setDriverProperties(new Properties() {{
            put("user", "root");
            put("password", "root");
            put("databaseName", databaseName);
        }});
        dataSource.init();
        return dataSource;
    }
}
