package com.globalegrow.bigdata.job.handler;

import com.alibaba.datax.common.constant.CommonConstant;
import com.alibaba.datax.common.element.DataXJob;
import com.alibaba.datax.common.element.DataXReport;
import com.alibaba.datax.common.job.DataXJobManager;
import com.alibaba.datax.common.statistics.VMInfo;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.Engine;
import com.alibaba.datax.core.job.meta.State;
import com.alibaba.datax.core.util.ConfigParser;
import com.alibaba.datax.core.util.ExceptionTracker;
import com.alibaba.datax.core.util.container.CoreConstant;
import com.alibaba.datax.core.util.container.LoadUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.globalegrow.bigdata.cluster.service.AbstractDataXService;
import com.globalegrow.bigdata.common.enums.ReportStatus;
import com.globalegrow.bigdata.common.enums.RequestType;
import com.globalegrow.bigdata.common.utils.HttpUtils;
import com.globalegrow.bigdata.job.dto.DataXJobDTO;
import com.globalegrow.bigdata.report.dao.DataXReportDao;
import com.globalegrow.bigdata.report.service.DataXReportService;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/10 17:31
 **/
@Slf4j
@Component
@RefreshScope
public class DataXCommonHandler implements AbstractJobHandler {

    @Autowired
    private DataXReportService dataXReportService;

    @Autowired
    private DataXReportDao dataXReportDao;

    @Autowired
    private AbstractDataXService dataXService;

    @Autowired
    @Qualifier("nacosNameService")
    NamingService namingService;

    /**
     * datax目录默认为/user/local/services/datax，如果是windows系统自己在配置文件里面手动设置即可
     */
    @Value("${datax.home}")
    String dataXHome="/user/local/services/datax";

    @Value("${server.port}")
    String port;

    @Value("${spring.application.name}")
    String serviceName;

    /**
     * 在任务开始之前，先注册，并且校验任务是否在执行，如果在执行则直接返回异常
     * @param dataXJobDTO
     */
    @Override
    public Configuration beforeStartJob(DataXJobDTO dataXJobDTO) throws Exception {

        //注册job
        dataXService.registJob(dataXJobDTO);

        //这里先设置好环境变量，方便搜索我们的datax的目录
        System.setProperty("datax.home",dataXHome);

        //加载configration文件，含job信息和core信息的装配
        Configuration configuration=ConfigParser.parseWithJobConf(dataXJobDTO.getJobConf());

        //将jobid加载到configration中（后续线程组全局都需要用到，这里很重要！！！）
        configuration.set(CoreConstant.DATAX_CORE_CONTAINER_JOB_ID, dataXJobDTO.getJobId());

        //初始化DataXReport，方便后续上报到admin
        initReport(configuration,dataXJobDTO);

        return configuration;
    }

    /**
     * 启动datax内置引擎，并开始推数
     * @param configuration
     * @return
     */
    @Override
    public Pair<DataXJob, DataXReport> startJob(Configuration configuration) {
        try{
            Engine engine = new Engine();
            //启动引擎
            engine.start(configuration);
      }catch (Throwable e){
            String errorLog="\n\n经DataX智能分析,该任务最可能的错误原因是:\n" + ExceptionTracker.trace(e);
            log.error(errorLog);
            Pair<DataXJob, DataXReport> dataxJobInfo=DataXJobManager.INSTANCE.getJob(configuration.getLong(CoreConstant.DATAX_CORE_CONTAINER_JOB_ID));
            dataxJobInfo.getValue().setExceptionLog(errorLog);
            dataxJobInfo.getValue().setJobState(State.FAILED.value());
            DataXJobManager.INSTANCE.refreshJob(dataxJobInfo);
        }finally {
            //任务结束后需要主动回收theadlocal
            LoadUtil.releasePluginConf(configuration.getLong(CoreConstant.DATAX_CORE_CONTAINER_JOB_ID));
        }
        return DataXJobManager.INSTANCE.getJob(configuration.getLong(CoreConstant.DATAX_CORE_CONTAINER_JOB_ID));
    }

    /**
     * 保证当前任务的快照
     * @param jobInfo
     */
    @Override
    public void beforeCompleteJob(Pair<DataXJob, DataXReport> jobInfo) {
        try{
            jobInfo.getValue().setStatus(jobInfo.getKey().getJobState());
            dataXReportDao.save(jobInfo.getValue());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 这里干两件事
     * 第一个：上报任务信息到nacos，防止集群其他机器无法再执行该任务（初始化的时候已经预留信息了，无论任务执行成功还是失败都需要删除该任务节点）
     * 第二个：上报任务信息到ods-admin，告知任务执行的成功或者失败信息由用户自己决策。如果是失败，提供错误信息供用户参考。
     * 两个事件需要分开做，分开持久化，方便失败后，后续定时任务扫描继续上报
     *
     * 说明：由于beforeCompleteJob里面已经对任务做过持久化了，所以这里datajob的快照肯定在derby里面已经存在，不需要做额外的处理，这里只管上报
     * @param jobInfo
     */
    @Override
    public void afterCompleteJob(Pair<DataXJob, DataXReport> jobInfo) {

        //上报日志到后端admin,可以自行实现这一块业务
        BeanUtils.copyProperties(jobInfo.getKey(),jobInfo.getValue());

        //上报nacos
        reportToNacos(jobInfo.getKey().getJobId());
        //上报admin
        reportToAdmin(jobInfo.getValue());
    }

    @Override
    public void beforeStopJob(Long jobId) {
    }

    /**
     * 终止任务
     * @param jobId
     * @return
     */
    @Override
    public void stopJob(Long jobId,Integer requestType) {

        Pair<DataXJob,DataXReport> dataXReport=DataXJobManager.INSTANCE.getJob(jobId);

        //如果本地任务不存在,则需要去远程服务器检查并终止任务
        if(dataXReport==null&&RequestType.FRIST_VISIT.getValue().equals(requestType)){
            forwardStopJob(jobId);
        }else{
            try {
                //首先上报nacos
                dataXService.completeJob(jobId);
            } catch (Exception e) {
                try {
                    //遇到异常需要对当前任务的快照进行持久化,同一个任务很可能也存在重复的记录，但是无关紧要，
                    // 后面只需要处理nacos里面的任务信息就好
                    if(dataXReport!=null&&dataXReport.getValue()!=null){
                        dataXReport.getValue().setJobState(State.KILLED.value());
                        dataXReport.getValue().setStatus(ReportStatus.FAILED.getValue());
                        dataXReportDao.save(dataXReport.getValue());
                    }
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterStopJob(Pair<DataXJob, DataXReport> jobInfo) {

    }

    @Override
    public Pair<DataXJob, DataXReport> reportResult(Long jobId) {
        return null;
    }

    @Override
    public void suspendJob(Long id) {

    }

    @Override
    public void continueJob(Long id) {

    }

    @Override
    public void forwardStartJob(DataXJobDTO dataXJobDTO) {
        try {
            List<Instance> list=namingService.getAllInstances(serviceName,CommonConstant.DEFAULT_GROUP);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到远程服务器去终止任务
     * @param jobId
     */
    @Override
    public void forwardStopJob(Long jobId) {
        try {
            DataXJob dataXJob=dataXService.getJob(jobId);
            //如果远程服务器也没有，则说明已经该job完成了
            if(dataXJob==null){
                return ;
            }
            StringBuffer url=new StringBuffer();
            url.append("http://").append(dataXJob.getHost()[0]).append(":").append(port)
                    .append("/datax/job/stop");
            Validate.isTrue(jobId > 0, "jobId > 0");
            log.info("任务【{}】不在本机，即将跳转【{}】，url为：{}",jobId,dataXJob.getHost()[0],url.toString());
            Map params = new HashMap(2);
            params.put("jobId",jobId);
            params.put("fordward", RequestType.FORWARD_VISIT.getValue());
            HttpUtils.INSTANCE.doPostRequest(url.toString(),JSONObject.toJSONString(params));
        } catch (Throwable e) {
            log.error(ExceptionTracker.trace(e));
        }
    }

    /**
     * 初始化报告
     * @param configuration
     * @param dataXJobDTO
     */
    private void initReport(Configuration configuration,DataXJobDTO dataXJobDTO){

        //打印vmInfo
        VMInfo vmInfo = VMInfo.getVmInfo();
        if (vmInfo != null) {
            log.info(vmInfo.toString());
        }

        log.info("\n" + Engine.filterJobConfiguration(configuration) + "\n");

        log.debug(configuration.toJSON());
        //记录当前jvm参数以及cpu，内存信息
        DataXJobManager.INSTANCE.getJob(dataXJobDTO.getJobId()).getValue().setVmInfo(vmInfo.totalString());
        DataXJobManager.INSTANCE.getJob(dataXJobDTO.getJobId()).getValue().setConfigurationInfo(Engine.filterJobConfiguration(configuration));
        BeanUtils.copyProperties(DataXJobManager.INSTANCE.getJob(dataXJobDTO.getJobId()).getKey(),dataXJobDTO);
    }

    /**
     * 上报任务信息到ods-admin
     * @param jobReport
     */
    @Override
    public void reportToAdmin(DataXReport jobReport){
        try {
            //将执行情况上报到ods-admin
            dataXReportService.report(jobReport);
            if(ReportStatus.FAILED.getValue().equals(jobReport.getStatus())||ReportStatus.UNTREATED.getValue().equals(jobReport.getStatus())){
                dataXReportDao.changeStatus(jobReport.getJobId(), ReportStatus.SUCCESSED.getValue());
            }
        } catch (Exception e) {
            //持久化任务快照,这里保存的
            dataXReportDao.changeStatus(jobReport.getJobId(), ReportStatus.FAILED.getValue());
            log.error(e.getMessage());
        }
    }

    /**
     * 上报任务信息到nacos
     * @param jobId
     */
    @Override
    public void reportToNacos(Long jobId){
        try {
            //更新nacos里面任务状态
            dataXService.completeJob(jobId);
            dataXReportDao.changeStatus(jobId, ReportStatus.SUCCESSED.getValue());
        } catch (Exception e) {
            //持久化任务快照
            dataXReportDao.changeStatus(jobId, ReportStatus.NACOS_UNREPORT.getValue());
            log.error(e.getMessage());
        }
    }
}
