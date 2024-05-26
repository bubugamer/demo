/*
package com.example.demo.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    // 当前使用的数据源标识
    public static ThreadLocal<String> name = new ThreadLocal<>();

    // 主数据源
    @Autowired
    private DataSource masterDataSource;

    // 从数据源
    @Autowired
    private DataSource slaveDataSource;

    @Override
    public void afterPropertiesSet() {
        // 为 targetDataSources 初始化所有数据源
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("W", masterDataSource);
        targetDataSources.put("R", slaveDataSource);

        super.setTargetDataSources(targetDataSources);
        super.setDefaultTargetDataSource(masterDataSource);

        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return name.get();
    }
}
*/

package com.example.demo.conf;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }
}
