package com.ryz2593.mapper.rws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ryz2593
 * @date 2019/4/10
 * @desc
 */
public class DynamicDataSourceHolder {
    public static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    public static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";


    /**
     * 获取路由的key
     * @return
     */
    public static String getRouteKey() {
        String routeKey = contextHolder.get();

        if (routeKey == null) {
            routeKey = DB_MASTER;
        }

        return routeKey;
    }

    /**
     * 定义设置路由key的方法
     * @param routeKey
     */
    public static void setRouteKey(String routeKey) {
        contextHolder.set(routeKey);
        LOGGER.debug("切换的数据源为： " + routeKey);
    }
}
