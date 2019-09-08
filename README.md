# datax-service
针对datax进行2次开发，实现data 以rpc的方式传递json配置调用推数服务，同时修复datax多处bug。项目中也引入nacos作为服务的配置中心和注册中心； 同时项目内扩展了kafkawriter，rabbitmqwriter，esreader，hivereader。增强了hdfs插件，支持分区表推送，支持动态参数传递（例如时间实现自增式抽取）。具体使用方式可以参照example模块。目前该服务已经稳定服务某上市公司半年，累计总任务数100+ ，日推送数据过10亿。具体如何使用，如何做插件开发以及datax底层原理，请关注https://blog.csdn.net/xiaoyao1999hn

##打包方式
    $ mvn -U clean package assembly:assembly -Dmaven.test.skip=true
    
#如何启动服务
    运行datax-servicer模块下的App即可

#
如果有用到nacos则需要加入配置datax-service.yml具体如下

     logging:
        level:
          root: debug
          org.springframework.cloud.gateway.filter.LoadBalancerClientFilter: TRACE
          log4j.logger.org.springframework.jdbc.core.JdbcTemplate: debug
          com.alibaba.nacos.client.naming: error
      spring:
        cloud:
          loadbalancer:
            retry:
              enabled: true
        datasource:
          type: com.alibaba.druid.pool.DruidDataSource
          driverClassName: org.apache.derby.jdbc.EmbeddedDriver
          url: jdbc:derby:${classpath:resource}/datax_metas_db;create=true
          username: root
          password: 123456
      
      ribbon:
        # 同一实例最大重试次数，不包括首次调用
        MaxAutoRetries: 1
        # 重试其他实例的最大重试次数，不包括首次所选的server
        MaxAutoRetriesNextServer: 2
        # 是否所有操作都进行重试
        OkToRetryOnAllOperations: false
      
      datax:
        home: D:/JavaProject/glbg-datax-service/target/datax/datax
        maxJobCount: 3 