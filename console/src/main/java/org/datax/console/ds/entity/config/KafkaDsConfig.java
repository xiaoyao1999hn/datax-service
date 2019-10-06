package org.datax.console.ds.entity.config;

import lombok.Data;

/**
 * kafka配置实体类
 * @author ChengJie
 * @desciption
 * @date 2019/5/6 20:16
 **/
@Data
public class KafkaDsConfig {

    /**
     * 用于建立与kafka集群连接的host/port组。
     */
    String bootstrapServers;

    /**
     * 用来唯一标识consumer进程所在组的字符串，如果设置同样的group id，表示这些processes都是属于同一个consumer group
     */
    String groupId;

    /**
     * 如果为真，consumer所fetch的消息的offset将会自动的同步到zookeeper。这项提交的offset将在进程挂掉时，由新的consumer使用。默认true。
     */
    String enableAutoCommit;

    /**
     * consumer向zookeeper提交offset的频率，单位是秒。默认60*1000
     */
    Integer autoCommitIntervalMs;

    /**
     * zookeeper中没有初始化的offset时，如果offset是以下值的回应
     *
     * lastest：自动复位offset为lastest的offset
     *
     * earliest：自动复位offset为earliest的offset
     *
     * none：向consumer抛出异常
     */
    String autoOffsetReset;

    /**
     * zookeeper 会话的超时限制。默认6000
     */
    Integer sessionTimeoutMs;

    /**
     * key序列化类
     */
    String keyDeserializer;

    /**
     * value序列化类
     */
    String valueDeserializer;

    /**
     * 此配置实际上代表了数据备份的可用性
     * acks=0： 设置为0表示producer不需要等待任何确认收到的信息。副本将立即加到socket buffer并认为已经发送。没有任何保障可以保证此种情况下server已经成功接收数据，同时重试配置不会发生作用
     *
     * acks=1： 这意味着至少要等待leader已经成功将数据写入本地log，但是并没有等待所有follower是否成功写入。这种情况下，如果follower没有成功备份数据，而此时leader又挂掉，则消息会丢失。
     *
     * acks=all： 这意味着leader需要等待所有备份都成功写入日志，这种策略会保证只要有一个备份存活就不会丢失数据。这是最强的保证。
     */
    String acks;

    /**
     * 设置大于0的值将使客户端重新发送任何数据，一旦这些数据发送失败。注意，这些重试与客户端接收到发送错误时的重试没有什么不同
     */
    Integer retries;

    /**
     * producer将试图批处理消息记录，以减少请求次数。这将改善client与server之间的性能。这项配置控制默认的批量处理消息字节数。
     */
    Long batchSize;

    /**
     * producer组将会汇总任何在请求与发送之间到达的消息记录一个单独批量的请求。通常来说，这只有在记录产生速度大于发送速度的时候才能发生
     */
    Integer lingerMs;

    /**
     * producer可以用来缓存数据的内存大小。如果数据产生速度大于向broker发送的速度，producer会阻塞或者抛出异常，以“block.on.buffer.full”来表明
     */
    Long bufferMemory;

    /**
     * 队列名称
     */
    String topicName;


}
