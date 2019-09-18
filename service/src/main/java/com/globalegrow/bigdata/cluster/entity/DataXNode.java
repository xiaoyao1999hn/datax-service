package com.globalegrow.bigdata.cluster.entity;

import com.alibaba.datax.common.element.DataXJob;
import lombok.Data;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/16 9:58
 **/
@Data
public class DataXNode {

    String host;

    int port;

    List<DataXJob> jobs;

}
