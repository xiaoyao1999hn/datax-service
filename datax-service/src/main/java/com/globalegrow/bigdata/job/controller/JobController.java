package com.globalegrow.bigdata.job.controller;

import com.globalegrow.bigdata.common.entity.ResponeResult;
import com.globalegrow.bigdata.job.dto.DataXJobDTO;
import com.globalegrow.bigdata.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * datax任务业务类
 * @author ChengJie
 * @desciption
 * @date 2019/7/10 10:01
 **/
@RestController
@RequestMapping("/datax/job")
public class JobController {

    @Autowired
    JobService jobService;

    @PostMapping("/start")
    public ResponeResult startJob(@RequestBody DataXJobDTO dataXJobDTO){
        try {
            jobService.startJob(dataXJobDTO);
        }  catch (Exception e) {
            e.printStackTrace();
            return ResponeResult.error(500,e.getMessage());
        }
        return ResponeResult.ok();
    }

    @PostMapping("/stop")
    public ResponeResult stopJob(@RequestBody DataXJobDTO dataXJobDTO){
        try {
            jobService.stopJob(dataXJobDTO.getJobId(),dataXJobDTO.getRequestType());
        } catch (Exception e) {
            return ResponeResult.error(500,e.getMessage());
        }
        return ResponeResult.ok();
    }

    @GetMapping("/list")
    public ResponeResult list(Map params){
        return ResponeResult.ok().put("list",jobService.list(params)).put("count",jobService.count(params));
    }

}
