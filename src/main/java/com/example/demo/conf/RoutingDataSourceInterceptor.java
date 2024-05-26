/*
package com.example.demo.conf;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;
import java.util.concurrent.Executor;

@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
                @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
                @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        }
)

@Slf4j
public class DynamicDataSourcePlugin implements Interceptor {
        @Override
        public Object intercept(Invocation invocation) throws Throwable {
                Object[] objects = invocation.getArgs();

                MappedStatement mappedStatement = (MappedStatement) objects[0];

                if(mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)){
                        log.info("readonly");
                        DynamicDataSource.name.set("R");
                }else{
                        log.info("write");
                        DynamicDataSource.name.set("W");
                }
                return invocation.proceed();
        }

        @Override
        public Object plugin(Object target) {
                return Plugin.wrap(target, this);
        }

        @Override
        public void setProperties(Properties properties) {

        }
}
*/

package com.example.demo.conf;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({
        @Signature(type = org.apache.ibatis.executor.Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = org.apache.ibatis.executor.Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = org.apache.ibatis.executor.Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Slf4j
public class RoutingDataSourceInterceptor implements Interceptor {

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
                Object[] objects = invocation.getArgs();
                MappedStatement mappedStatement = (MappedStatement) objects[0];

                if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                        log.info("Switching to read-only data source");
                        DynamicDataSource.setDataSourceKey("slave");
                } else {
                        log.info("Switching to write data source");
                        DynamicDataSource.setDataSourceKey("master");
                }

                return invocation.proceed();
        }

        @Override
        public Object plugin(Object target) {
                return Plugin.wrap(target, this);
        }

        @Override
        public void setProperties(Properties properties) {
                // 可以在这里读取并处理插件配置
        }
}