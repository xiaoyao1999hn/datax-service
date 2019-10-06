package org.datax.console.ds.entity.config;

import lombok.Data;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/6 20:18
 **/
@Data
public class EsDsConfig {

    /**
     * 主机ip
     */
    String host;

    /**
     * 端口
     */
    String port;

    /**
     * 协议
     */
    String scheme;

    /**
     * 超时
     */
    Integer timeout;

    String accessId;

    String accessKey;

}
