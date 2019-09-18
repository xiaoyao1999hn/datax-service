package com.globalegrow.bigdata.job.service.impl;

import com.alibaba.datax.common.element.DataXJob;
import com.alibaba.datax.common.element.DataXReport;
import com.alibaba.datax.common.job.DataXJobManager;
import com.alibaba.datax.common.util.Configuration;
import com.globalegrow.bigdata.job.dto.DataXJobDTO;
import com.globalegrow.bigdata.job.handler.AbstractJobHandler;
import com.globalegrow.bigdata.job.service.JobService;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/10 10:14
 **/
@Service
@Slf4j
public class JobServiceImpl implements JobService {

    @Autowired
    private AbstractJobHandler jobHandler;

    @Autowired
    @Qualifier("dataXExecutorPool")
    private ThreadPoolExecutor dataXExecutorPool;

    @Override
    public void startJob(DataXJobDTO dataXJobDTO) {
        try{
            dataXExecutorPool.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        //先校验任务执行的情况,防止重复执行,如果该任务未执行则返回任务执行需要的配置信息
                        Configuration configuration=jobHandler.beforeStartJob(dataXJobDTO);
                        Pair<DataXJob,DataXReport> dataXReport=jobHandler.startJob(configuration);
                        //无论job执行失败还是成功，都先持久化下来
                        jobHandler.beforeCompleteJob(dataXReport);
                        //接着开始走上报nacos和ods-admin的流程
                        jobHandler.afterCompleteJob(dataXReport);
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            });
        }catch (RejectedExecutionException e) {
            jobHandler.forwardStartJob(dataXJobDTO);
        }
    }

    @Override
    public void stopJob(Long jobId,Integer requestType) {
        try{
            dataXExecutorPool.execute(new Runnable() {
                @Override
                public void run() {
                    jobHandler.beforeStopJob(jobId);
                    jobHandler.stopJob(jobId,requestType);
                }
            });
        }catch (RejectedExecutionException e){
            jobHandler.forwardStopJob(jobId);
        }

//        //为空说明该服务不在本机
//        Pair<DataXJob,DataXReport> dataXReport=DataXJobManager.INSTANCE.getJob(jobId);
//        if(dataXReport!=null){
//            jobHandler.afterStopJob(dataXReport);
//        }
    }

    @Override
    public List<DataXJob> list(Map params) {
        return DataXJobManager.INSTANCE.jobList();
    }

    @Override
    public int count(Map params) {
        return DataXJobManager.INSTANCE.jobCount();
    }

    @Override
    public DataXJob get(Long jobId) {
        return null;
    }

}
