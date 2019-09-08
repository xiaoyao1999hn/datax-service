package com.globalegrow.bigdata.cluster.service;

import com.alibaba.datax.common.element.DataXJob;
import com.globalegrow.bigdata.cluster.entity.DataXNode;
import com.globalegrow.bigdata.job.dto.DataXJobDTO;

import java.util.List;

public interface AbstractDataXService {

    List<DataXNode> getNodes();

    DataXJob getJob(Long jobId) throws Exception;

    void registJob(DataXJobDTO dataXJobDTO) throws Exception;

    void completeJob(Long jobId) throws Exception;

    void refreshJob(DataXJob dataXJob) throws Exception;

}
