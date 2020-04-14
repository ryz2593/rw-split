# rw-split
MySQL read and write seperate

**包含数据源加密工具类**

数据库读写分离

在spring中配置多个数据源，增删改走主库，查询走从库

<img src="https://img-blog.csdn.net/20130429111107055" />

**编写DynamicDataSource类，继承AbstractRoutingDataSource类，重写determineCurrentLookupKey()方法**

**编写读写分离路由插件类DynamicDataSourcePlugin，实现Interceptor接口，根据mybatis sql执行机制，进行拦截，使用不同的策略（master, slave）**

**mysql主从复制用途**

实时灾备，用于故障切换

读写分离，提供查询服务

备份，避免影响业务

**主从部署必要条件：**

主库开启binlog日志（设置log-bin参数）

主从server-id不同

从库服务器能连通主库

# 一、MySQL主从配置原理

**1. 主从工作原理**

<img src="https://yqfile.alicdn.com/img_9a4f1bb77468f23b578067efe1fcc05e.png" />

从库生成两个线程，一个I/O线程，一个SQL线程；

i/o线程去请求主库 的binlog，并将得到的binlog日志写到relay log（中继日志） 文件中；

主库会生成一个 log dump 线程，用来给从库 i/o线程传binlog；

SQL 线程，会读取relay log文件中的日志，并解析成具体操作，来实现主从的操作一致，而最终数据一致；

**2. 配置MySQL主从具体实现**

**2.1 在服务器上面安装两个MySQL数据库**

**2.1.1 Master机器配置**

vim /etc/my.cnf

1 # 打开log-bin日志 

2 server-id=1

3 log_bin=master-bin #打开二进制文件

配置完成保存后，重启mysql服务器，再次登录MYSQL服务器

执行show master status; ## 查看主服务器的log-bin日志

记录下File的名称和Position的位置

File = master-bin.000001

Position = 154

**2.1.2 Slave机器配置**

1 ## 打开relay-log 日志

2 server-id = 2

配置完成保存后，重启mysql服务器，再次登录MYSQL服务器

解决Connection xxxx.reties 18 异常：

必须在master端创建同步的账号和密码

create user u_root identified by '123456';

grant replication slave on *.* to 'u_root'@'%';

flush privileges;

在从服务器上执行如下SQL:

change master to master_host='192.168.3.171',master_user='u_root',master_password='123456',master_log_file='master-bin.000002',master_log_pos=310

NOTE:这两个参数master_log_file=master-bin.000002,master_log_pos=154必须和主库一致

**2.1.3 启动主从配置**

mysql > start slave

//检查slave是否配置成功

mysql > show slave status \G

开启远程访问：

1 -- 创建用户、密码及权限范围 第一个root为用户名 @后为适用的主机 '%'表示所有电脑都可以访问连接，第二个root为密码

2 mysql>GRANT ALL PRIVILEGES ON *.* TO 'root'@'%'IDENTIFIED BY '123456' WITH GRANT OPTION;

3 Query ok, 0 rows affected

4

5 --立即生效

6 mysql > flush privileges;





# 二、业务代码的实现
