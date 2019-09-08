package com.alibaba.datax.common.element;

/**
 *
 */
public class DataXReport {

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务执行状态
     */
    private int jobState;

    /**
     * 进度
     */
    private Double progress;


    /**
     * jvm快照
     */
    private String vmInfo;

    /**
     * 任务组数
     */
    private Integer taskCount;

    /**
     * 任务开始时间
     */
    private long startTimeStamp;

    /**
     * 任务结束时间
     */
    private long endTimeStamp;

    /**
     * 数据传输开始时间
     */
    private long startTransferTimeStamp;

    /**
     * 数据传输结束时间
     */
    private long endTransferTimeStamp;

    /**
     * 接收到任务的时间
     */
    private long createTime;

    /**
     * 任务耗时
     */
    private String runTimes;
    /**
     * 任务平均流量
     */
    private String avgFlow;
    /**
     * 任务写入速度
     */
    private String speed;

    /**
     * 传输总记录数
     */
    private String totalRecordCount;

    /**
     * 写入数据总数
     */
    private String totalWriteRecordCount;

    /**
     * 读出总数据大小
     */
    private String totalReadBytes;

    /**
     * 写入总数据大小
     */
    private String totalWriteBytes;

    /**
     * 传输失败记录数
     */
    private String totalFailRecordCount;

    /**
     * 上报状态：0-未上报；1-上报成功；2-上报失败
     */
    private Integer status;

    /**
     * 上报失败次数
     */
    private Integer failCount;

    /**
     * 异常日志
     */
    private String exceptionLog;

    /**
     * 执行日志
     */
    private String logStatistics;

    /**
     * 配置信息
     */
    private String configurationInfo;

    /**
     * 备注，目前展示用于标记在哪台机器上运行
     */
    private String remark;

    public DataXReport(){

    }

    public DataXReport(Long jobId){
        this.setJobId(jobId);
        this.setProgress(0d);
        this.setCreateTime(System.currentTimeMillis());
        this.setAvgFlow("0");
        this.setFailCount(0);
        this.setRunTimes("0");
        this.setSpeed("0");
        this.setStatus(0);
        this.setTaskCount(0);
        this.setStartTimeStamp(System.currentTimeMillis());
        this.setEndTimeStamp(startTimeStamp);
        this.setStartTransferTimeStamp(startTimeStamp);
        this.setEndTransferTimeStamp(startTimeStamp);
        this.setTotalRecordCount("0");
        this.setTotalFailRecordCount("0");
        this.setVmInfo("");
        this.setConfigurationInfo("");
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

    public String getVmInfo() {
        return vmInfo;
    }

    public void setVmInfo(String vmInfo) {
        this.vmInfo = vmInfo;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public long getStartTransferTimeStamp() {
        return startTransferTimeStamp;
    }

    public void setStartTransferTimeStamp(long startTransferTimeStamp) {
        this.startTransferTimeStamp = startTransferTimeStamp;
    }

    public long getEndTransferTimeStamp() {
        return endTransferTimeStamp;
    }

    public void setEndTransferTimeStamp(long endTransferTimeStamp) {
        this.endTransferTimeStamp = endTransferTimeStamp;
    }

    public String getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(String runTimes) {
        this.runTimes = runTimes;
    }

    public String getAvgFlow() {
        return avgFlow;
    }

    public void setAvgFlow(String avgFlow) {
        this.avgFlow = avgFlow;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(String totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public String getTotalFailRecordCount() {
        return totalFailRecordCount;
    }

    public void setTotalFailRecordCount(String totalFailRecordCount) {
        this.totalFailRecordCount = totalFailRecordCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getExceptionLog() {
        return exceptionLog;
    }

    public void setExceptionLog(String exceptionLog) {
        this.exceptionLog = exceptionLog;
    }

    public String getLogStatistics() {
        return logStatistics;
    }

    public void setLogStatistics(String logStatistics) {
        this.logStatistics = logStatistics;
    }

    public String getConfigurationInfo() {
        return configurationInfo;
    }

    public void setConfigurationInfo(String configurationInfo) {
        this.configurationInfo = configurationInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotalWriteRecordCount() {
        return totalWriteRecordCount;
    }

    public void setTotalWriteRecordCount(String totalWriteRecordCount) {
        this.totalWriteRecordCount = totalWriteRecordCount;
    }

    public String getTotalReadBytes() {
        return totalReadBytes;
    }

    public void setTotalReadBytes(String totalReadBytes) {
        this.totalReadBytes = totalReadBytes;
    }

    public String getTotalWriteBytes() {
        return totalWriteBytes;
    }

    public void setTotalWriteBytes(String totalWriteBytes) {
        this.totalWriteBytes = totalWriteBytes;
    }
}
