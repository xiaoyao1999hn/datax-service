package com.globalegrow.bigdata.report.service.hystrix;

import com.alibaba.datax.common.element.DataXReport;
import com.globalegrow.bigdata.common.entity.ResponeResult;
import com.globalegrow.bigdata.report.dao.DataXReportDao;
import com.globalegrow.bigdata.report.service.DataXReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/15 17:53
 **/
@Component
public class DataXReportServiceHystrix  implements DataXReportService {

    @Autowired
    private DataXReportDao reportDao;

    @Override
    public ResponeResult report(DataXReport dataXReport) {
        //触发熔断之后保存报告快照
        try {
            reportDao.save(dataXReport);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ResponeResult.ok();
    }
}
