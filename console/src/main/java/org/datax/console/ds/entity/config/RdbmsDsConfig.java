package org.datax.console.ds.entity.config;

import lombok.Data;

/**
 *
 * @author ChengJie
 * @desciption
 * @date 2019/5/6 20:17
 **/
@Data
public class RdbmsDsConfig {

    /**
     * 0-mysql；1-oracle；2-sqlserver；3-db2
     */
    Integer type;

    /**
     * 主机名
     */
    String host;

    /**
     * 端口
     */
    String port;
    /**
     * 数据库连接池名称
     */
    String dsPoolName;

    /**
     * 驱动名
     */
    String driverName;

    /**
     * 用户名
     */
    String userName;

    /**
     * 密码
     */
    String password;

    /**
     * 实例名称
     */
    String instanceName;

    /**
     * 初始连接数
     */
    private Integer initialSize;
    /**
     * 最小空闲
     */
    private Integer minIdle;
    /**
     * 最大连接数
     */
    private Integer maxActive;
    /**
     * 连接等待超时时间
     */
    private Integer maxWait;
    /**
     * 检测需要关闭的空闲连接，单位是毫秒
     */
    private Integer runsMill;
    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private Integer minEvictableMill;
    /**
     * 验证语句
     */
    private String validateQuery;


}
