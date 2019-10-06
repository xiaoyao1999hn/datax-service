package org.datax.console.ds.entity.config;

import lombok.Data;

/**
 * hive的参数配置
 * @author ChengJie
 * @desciption
 * @date 2019/5/6 20:18
 **/
@Data
public class HiveDsConfig extends RdbmsDsConfig {

    /**
     * 连接hdfs抽数时的登陆用户
     */
    String hdfsUserName;

}
