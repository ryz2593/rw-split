package com.ryz2593.mapper.rws;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Properties;

/**
 * @author ryz2593
 * @date 2019/4/10
 * @desc 读写分离路由插件
 */
@Intercepts({
        //update 增，删，改
        //query  查询
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class DynamicDataSourcePlugin implements Interceptor {
    public static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourcePlugin.class);
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //判断操作是否存在事务
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        //默认让routeKey为MASTER
        String routeKey = DynamicDataSourceHolder.DB_MASTER;

        //第一个参数类型为MappedStatement对象， 第二个为传入的参数
        Object[] objects = invocation.getArgs();
        MappedStatement statement = (MappedStatement) objects[0];
        if (!active) {
            //判断读方法
            if (statement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //如果在语句中使用了select last insert_id函数
                if (statement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    routeKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    routeKey = DynamicDataSourceHolder.DB_SLAVE;
                }
            } else {
                routeKey = DynamicDataSourceHolder.DB_MASTER;
            }
        } else {
            //带事务操作一定在主库中操作
            routeKey = DynamicDataSourceHolder.DB_MASTER;
        }

        //设置确定的路由key
        DynamicDataSourceHolder.setRouteKey(routeKey);
        LOGGER.debug("使用{}方法, 使用[{}]策略，执行SQL命令为{}", invocation.getMethod().getName(),routeKey,statement.getSqlCommandType().name());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
