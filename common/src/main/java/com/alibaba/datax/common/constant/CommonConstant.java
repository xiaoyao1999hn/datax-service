package com.alibaba.datax.common.constant;

public final class CommonConstant {
    /**
     * 用于插件对自身 split 的每个 task 标识其使用的资源，以告知core 对 reader/writer split 之后的 task 进行拼接时需要根据资源标签进行更有意义的 shuffle 操作
     */
    public static String LOAD_BALANCE_RESOURCE_MARK = "loadBalanceResourceMark";

    /**
     * nacos中注册的任务配置id
     */
    public static final String JOB_DATAID="datax-jobs.yml";

    /**
     * nacos中注册的节点配置id
     */
    public static final String NODES_DATAID="datax-nodes.yml";

    /**
     * 默认分组
     */
    public static final String DEFAULT_GROUP="DATAX_GROUP";
}
