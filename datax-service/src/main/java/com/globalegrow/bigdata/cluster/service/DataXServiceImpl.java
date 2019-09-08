package com.globalegrow.bigdata.cluster.service;

import com.alibaba.datax.common.constant.CommonConstant;
import com.alibaba.datax.common.element.DataXJob;
import com.alibaba.datax.common.element.DataXReport;
import com.alibaba.datax.common.exception.CommonErrorCode;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.job.DataXJobManager;
import com.alibaba.datax.core.job.meta.State;
import com.alibaba.datax.core.util.FrameworkErrorCode;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.globalegrow.bigdata.cluster.entity.DataXNode;
import com.globalegrow.bigdata.common.utils.YamlUtils;
import com.globalegrow.bigdata.job.dto.DataXJobDTO;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DataXServiceImpl implements AbstractDataXService {

//    @Autowired
//    @NacosInjected
//    NamingService namingService;

    @Autowired
    @Qualifier("dataXJobConfigService")
    ConfigService configService;

    @Value("spring.application.name")
    String serviceName;

    @Override
    public List<DataXNode> getNodes() {
//        try {
//            List<Instance> list = namingService.getAllInstances(serviceName);
//            List<DataXNode> nodes = list.stream().map(x -> {
//                DataXNode temp = new DataXNode();
//                temp.setHost(x.getIp());
//                temp.setPort(x.getPort());
//                return temp;
//            }).collect(Collectors.toList());
//
//            return nodes;
//        } catch (NacosException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public DataXJob getJob(Long jobId) throws Exception {
        return getAllJob().get(jobId.intValue());
    }

    @Override
    public void registJob(DataXJobDTO dataXJobDTO) throws Exception {

        //校验任务格式以及是否已经在执行了
        Map<Integer, DataXJob> allJob=doValidate(dataXJobDTO.getJobId());

        //dataXJob初始化
        Pair<DataXJob,DataXReport> dataXJob=DataXJobManager.INSTANCE.getJob(dataXJobDTO.getJobId());
        if(dataXJob==null){
            dataXJob=new Pair<>(new DataXJob(dataXJobDTO.getJobId()),new DataXReport(dataXJobDTO.getJobId()));
            //校验完毕后在缓存中注册任务信息
            dataXJob.getKey().setJobState(State.RUNNING.value());
        }
        //复制jobname等属性
        BeanUtils.copyProperties(dataXJobDTO,dataXJob.getKey());

        //更新远程服务器job信息
        allJob.put(dataXJob.getKey().getJobId().intValue(), dataXJob.getKey());

        //注册到远程服务器
        publishJob(YamlUtils.INSTANCE.yamlToString(allJob));

        //本地完成注册
        DataXJobManager.INSTANCE.registJob(dataXJob);
    }

    @Override
    public void completeJob(Long jobId) throws Exception {
        Map<Integer, DataXJob> allJob = getAllJob();
        //如果任务不存在则不需要去变动nacos上的配置
        if (allJob == null || allJob.get(jobId.intValue()) == null) {
            return;
        }
        //清除该任务
        allJob.remove(jobId.intValue());
        //发布到远程服务器
        publishJob(YamlUtils.INSTANCE.yamlToString(allJob));

        //本地再清除该任务
        DataXJobManager.INSTANCE.removeJob(jobId);
        //然后回收线程池资源
//        DataXJobManager.INSTANCE.removeJobThreadPool(jobId);
    }

    @Override
    public void refreshJob(DataXJob dataXJob) throws  Exception{
        Map<Integer, DataXJob> allJob = getAllJob();
        allJob.put(dataXJob.getJobId().intValue(),dataXJob);
        //发布到远程服务器
        publishJob(YamlUtils.INSTANCE.yamlToString(allJob));
    }

    private Map<Integer, DataXJob> doValidate(Long jobId) throws Exception {
        //先检查本地执行情况
        Pair<DataXJob,DataXReport> dataXJob=DataXJobManager.INSTANCE.getJob(jobId);
        if(dataXJob!=null&&State.RUNNING.value()==dataXJob.getKey().getJobState()){
            throw DataXException.asDataXException(
                    FrameworkErrorCode.RUNTIME_ERROR, new RuntimeException("任务已经在本地执行了，请稍后再试！"));
        }

        //然后再检查集群中是否存在该任务在执行
        Map<Integer, DataXJob> jobMap=getAllJob();
        DataXJob job= jobMap.get(jobId.intValue());
        if(job!=null&&State.RUNNING.value()==job.getJobState()){
            throw new DataXException(CommonErrorCode.CONFIG_ERROR,"当前任务在正在远程服务器【"+job.getRemark()+"】执行");
        }
        return jobMap;
    }

    /**
     * 获取所有的任务
     * @return
     * @throws NacosException
     */
    private Map<Integer, DataXJob> getAllJob() throws NacosException {
        String content = configService.getConfig(CommonConstant.JOB_DATAID, CommonConstant.DEFAULT_GROUP, 5000);
        if(StringUtils.isEmpty(content)){
            return new HashMap<>();
        }
        Map<Integer, DataXJob> jobMap = YamlUtils.INSTANCE.stringToObject(content);
        if (jobMap == null) {
            return new HashMap<>();
        }
        return jobMap;
    }

    /**
     * 发布配置到nacos
     * @param content
     * @throws Exception
     */
    private void publishJob(String content) throws Exception {
        configService.publishConfig(CommonConstant.JOB_DATAID, CommonConstant.DEFAULT_GROUP,content);
    }
}
