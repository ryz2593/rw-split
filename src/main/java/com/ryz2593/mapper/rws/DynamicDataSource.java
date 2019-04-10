package com.ryz2593.mapper.rws;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author ryz2593
 * @date 2019/4/10
 * @desc
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 在spring容器中查询对应key来应用为数据源（key一定是数据源的）
     * {@link #resolveSpecifiedLookupKey} method.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getRouteKey();
    }
}
