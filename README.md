# rw-split
MySQL read and write seperate

数据库读写分离

在spring中配置多个数据源，增删改走主库，查询走从库

<img src="https://img-blog.csdn.net/20130429111107055" />

编写DynamicDataSource类，继承AbstractRoutingDataSource类，重写determineCurrentLookupKey()方法

编写读写分离路由插件类DynamicDataSourcePlugin，实现Interceptor接口，根据mybatis sql执行机制，进行拦截，使用不同的策略（master, slave）
