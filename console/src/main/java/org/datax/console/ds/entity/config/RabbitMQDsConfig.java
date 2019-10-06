package org.datax.console.ds.entity.config;

import lombok.Data;

/**
 * rabbitMq 配置实体类
 * @author ChengJie
 * @desciption
 * @date 2019/5/6 20:16
 **/
@Data
public class RabbitMQDsConfig {

    /**
     * 主机名
     */
    String host;

    /**
     * 端口
     */
    String port;

    /**
     * 用户名
     */
    String userName;

    /**
     * 密码
     */
    String password;

    /**
     * 超时时间
     */
    Integer timeout;

    /**
     * 虚拟主机名
     */
    String vhost;

    /**
     * 交换器
     */
    String exchange;

    /**
     * 路由key
     */
    String routeKey;

    /**
     * server push消息时的队列长度 - 同一时刻服务器只会发一条消息给消费者  - 非必需
     */
    Integer basicQos;

    /**
     * 消息是否确认
     */
    Integer ack;

    /**
     * 队列名称
     */
    String queueName;
}
