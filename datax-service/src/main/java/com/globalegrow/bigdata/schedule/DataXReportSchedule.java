package com.globalegrow.bigdata.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/15 17:55
 **/
@Component
public class DataXReportSchedule {

    /**
     * 10s执行一次，上报哪些失败的推数日志
     */
    @Scheduled(fixedRate = 10000)
    public void reportFailLog() {

    }

    /**
     * 实时上报心跳及job运行情况
     */
    public void heartBeat(){

    }

}
