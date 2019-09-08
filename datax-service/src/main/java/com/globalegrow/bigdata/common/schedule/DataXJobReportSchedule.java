package com.globalegrow.bigdata.common.schedule;

import com.globalegrow.bigdata.job.handler.AbstractJobHandler;
import com.globalegrow.bigdata.report.dao.DataXReportDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 这里主要是定时取检测那种上报失败的任务报告，
 * @author ChengJie
 * @desciption
 * @date 2019/7/24 14:49
 **/
//@Component
@Slf4j
public class DataXJobReportSchedule {

    @Autowired
    DataXReportDao dataXReportDao;

    @Autowired
    private AbstractJobHandler jobHandler;

//    @Scheduled(fixedRate=10000)
//    private void reportFailedJob() {
//        List<DataXReport>  list=dataXReportDao.getReportFailedJobList();
//        log.info("当前存在{}条上报失败的数据",list.size());
//        list.forEach(x->{
//            if(ReportStatus.FAILED.getValue().equals(x.getStatus())||ReportStatus.NACOS_UNREPORT.getValue().equals(x.getStatus())){
//                jobHandler.reportToNacos(x.getJobId());
//                jobHandler.reportToAdmin(x);
//                log.info("任务【{}-{}】上报成功",x.getJobName(),x.getJobId());
//            }
//        });
//    }

}
