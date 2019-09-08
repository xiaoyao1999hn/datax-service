package com.globalegrow.datax.plugin.writer.rabbitmqwriter;

import com.alibaba.datax.common.util.Configuration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/4/27 16:25
 **/
public class RabbitmqUtil {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqUtil.class);

    private RabbitmqUtil(){}

    public Connection getConn(Configuration conf){
        //创建一个新的连接
        Connection connection = null;
        try {
            log.info("当前配置信息如下：【host:{}，port:{}，username:{}，password:{}，vhost:{},queueName:{},exchangeName:{},routeKey:{}】",
                    conf.getString("host"),
                    conf.getString("port"),
                    conf.getString("username"),
                    conf.getString("password"),
                    conf.getString("vhost"),
                    conf.getString("queueName"),
                    conf.getString("exchangeName"),
                    conf.getString("routeKey") );
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(conf.getString("host"));
            factory.setPort(Integer.parseInt(conf.getString("port")));
            factory.setUsername(conf.getString("username"));
            factory.setPassword(conf.getString("password"));
            factory.setConnectionTimeout(3000);
            factory.setVirtualHost(conf.getString("vhost"));
            connection=factory.newConnection();
        } catch (IOException e) {
            log.error("获取rabbitmq连接异常：{}",e.getMessage());
        } catch (TimeoutException e) {
            log.error("获取rabbitmq连接异常：{}",e.getMessage());
        }
        return connection;
    }

    public Channel getChannel(Connection conn,Configuration conf){

        //创建一个新的连接
        Channel channel =null;
        try {
            channel = conn.createChannel();
            channel.queueBind(conf.getString("queueName"),conf.getString("exchangeName"),conf.getString("routeKey"));
        } catch (IOException e) {
            log.error("获取rabbitmq连接异常：{}",e.getMessage());
        }
        return channel;
    }


    public void closeConn(Connection conn, Channel channel){
        try {
            if(channel!=null&&channel.isOpen()){
                channel.close();
            }
            if(conn!=null&&conn.isOpen()){
                conn.close();
            }
        } catch (IOException e) {
            log.error("关闭rabbitmq连接异常：{}",e.getMessage());
        } catch (TimeoutException e) {
            log.error("关闭rabbitmq连接超时：{}",e.getMessage());
        }
    }

//    public void send(String data,Configuration conf){
//        try {
//            Connection connection=getConn(conf);
//            Channel channel =getChannel(connection,conf);
//            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).
//                    contentEncoding("UTF-8").build();
//            channel.basicPublish(conf.getString("exchangeName"),conf.getString("routeKey"),properties,data.getBytes());
//            closeConn(connection,channel);
//        } catch (IOException e) {
//            log.error("发送消息失败：{}",e.getMessage());
//        }
//    }
//
//    public void send(String data,Configuration conf,Connection conn,Channel channel){
//        try {
//            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).
//                    contentEncoding("UTF-8").build();
//            channel.basicPublish(conf.getString("exchangeName"),conf.getString("routeKey"),properties,data.getBytes());
//            closeConn(conn,channel);
//        } catch (IOException e) {
//            log.error("发送消息失败：{}",e.getMessage());
//        }
//    }

    public static RabbitmqUtil getInstance(){
        return RabbitmqUtilHandler.instance;
    }

    private static class RabbitmqUtilHandler{
        public static RabbitmqUtil instance=new RabbitmqUtil();
    }

}
