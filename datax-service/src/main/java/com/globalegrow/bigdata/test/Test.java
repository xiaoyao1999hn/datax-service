package com.globalegrow.bigdata.test;

import com.alibaba.datax.common.util.BytesUtil;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/5 15:29
 **/
public class Test {
    public static  void main(String[] args){

        Long test=35124L*1024L;

        System.out.println(BytesUtil.getBytes(test));

    }
}
