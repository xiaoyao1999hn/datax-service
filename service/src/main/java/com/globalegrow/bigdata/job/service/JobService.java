package com.globalegrow.bigdata.job.service;

import com.alibaba.datax.common.element.DataXJob;
import com.globalegrow.bigdata.job.dto.DataXJobDTO;

import java.util.List;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/10 10:14
 **/
public interface  JobService   {

    /**
     * 启动任务
     * @param dataXJobDTO
     */
    void startJob(DataXJobDTO dataXJobDTO) throws Exception;

    /**
     * 结束任务
     * @param jobId
     */
    void stopJob(Long jobId ,Integer requestType) throws Exception;

    /**
     * 根据条件查询对应任务
     * @param params
     * @return
     */
    List<DataXJob> list(Map params);

    /**
     * 获取符合条件的任务数量
     * @param params
     * @return
     */
    int count(Map params);

    /**
     * 获取任务
     * @param jobId
     * @return
     */
    DataXJob get(Long jobId);


}
