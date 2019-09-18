package com.globalegrow.bigdata.job.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/10 11:33
 **/
@Setter
@Getter
public class DataXJobDTO {

    /**
     * job id
     */
    Long jobId;

    /**
     * 任务名称
     */
    String jobName;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 任务生命周期，用于标记任务是否有效
     */
    private Long jobLifeCycle;

    /**
     * 任务配置
     */
    String jobConf;

    /**
     * 任务类型 0-首次访问，1-转发请求
     */
    Integer requestType;

}
