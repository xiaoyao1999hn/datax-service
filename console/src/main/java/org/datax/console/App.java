package org.datax.console;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/9/18 9:06
 **/
//@EnableTransactionManagement
@ServletComponentScan
//@MapperScan("com.globalegrow.bigdata.ods.admin.*.dao")
@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
public class App {
}
