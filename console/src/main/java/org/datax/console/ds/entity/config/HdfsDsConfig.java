package org.datax.console.ds.entity.config;

import lombok.Data;

import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/6 20:18
 **/
@Data
public class HdfsDsConfig extends RdbmsDsConfig {

    /**
     * 默认hdfs域名
     */
    String defaultFS;

    /**
     * hadoop配置
     */
    Map<String, String> hadoopConfig;
}
