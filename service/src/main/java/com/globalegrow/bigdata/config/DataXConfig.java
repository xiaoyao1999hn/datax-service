package com.globalegrow.bigdata.config;

import com.alibaba.datax.common.constant.CommonConstant;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.globalegrow.bigdata.cluster.listeners.DataXJobListener;
import com.globalegrow.bigdata.cluster.listeners.DataXNodeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/16 9:55
 **/
@Configuration
@Slf4j
public class DataXConfig {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private  String serverAddr;

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;

    @Value("${datax.maxJobCount}")
    private Integer maxJobCount;

    @Autowired
    private DataXJobListener dataXJobListener;

    @Autowired
    private DataXNodeListener dataXNodeListener;

    @Bean(name = "dataXJobConfigService")
    public ConfigService dataXJobConfigService() throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        properties.put("namespace",namespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        //添加配置改动监听器
        configService.addListener(CommonConstant.JOB_DATAID, CommonConstant.DEFAULT_GROUP, dataXJobListener);
        configService.addListener(CommonConstant.NODES_DATAID, CommonConstant.DEFAULT_GROUP, dataXNodeListener);
        return configService;
    }

    @Bean(name = "dataXExecutorPool")
    public ThreadPoolExecutor dataXExecutorService(){
        BlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3,  maxJobCount>3?maxJobCount:3,
                10, TimeUnit.SECONDS, queue ) ;
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy ());
        return pool;
    }

    @Bean(name = "nacosNameService")
    NamingService nacosNameService(){
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);
        try {
            NamingService naming = NamingFactory.createNamingService(properties);
            return naming;
        } catch (NacosException e) {
            log.error(e.getErrMsg());
        }
        return null;
    }

}
