package com.globalegrow.bigdata.report.service;

import com.alibaba.datax.common.element.DataXReport;
import com.globalegrow.bigdata.common.entity.ResponeResult;
import com.globalegrow.bigdata.report.service.hystrix.DataXReportServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/15 17:51
 **/
@FeignClient(name = "ods-admin-service", fallback = DataXReportServiceHystrix.class)
public interface DataXReportService {

    @PostMapping("/ods/rpc/admin/report/pushLog")
    ResponeResult report(DataXReport dataXReport);


}
