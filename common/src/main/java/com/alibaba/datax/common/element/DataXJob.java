package com.alibaba.datax.common.element;

import com.alibaba.datax.common.util.HostUtils;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/10 10:01
 **/
public class DataXJob implements Serializable {

    /**
     * jobId
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务状态
     */
    private int jobState;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 任务生命周期，用于标记任务是否有效
     */
    private Long jobLifeCycle;

    /**
     * 备注，目前展示用于标记在哪台机器上运行
     */
    private String remark;

    /**
     * 任务所在机器,这里之所以是数组，是因为有可能服务器有双网卡
     */
    private String[] host;

    /**
     * 主机名
     */
    private String hostName;

    public DataXJob(){

    }

    public DataXJob(Long jobId){
        this.jobId=jobId;
        this.host=HostUtils.IP.split(",");
        this.hostName=HostUtils.HOSTNAME;
        StringBuffer buffer=new StringBuffer();
        buffer.append(hostName).append("-").append(Arrays.stream(this.host).collect(Collectors.toList()));
        this.remark=buffer.toString();
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getJobState() {
        return jobState;
    }

    public void setJobState(int jobState) {
        this.jobState = jobState;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getJobLifeCycle() {
        return jobLifeCycle;
    }

    public void setJobLifeCycle(Long jobLifeCycle) {
        this.jobLifeCycle = jobLifeCycle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String[] getHost() {
        return host;
    }

    public void setHost(String[] host) {
        this.host = host;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
