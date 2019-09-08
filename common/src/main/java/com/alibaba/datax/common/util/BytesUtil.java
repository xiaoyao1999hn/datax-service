package com.alibaba.datax.common.util;

import java.text.DecimalFormat;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/5 15:37
 **/
public class BytesUtil {

    public static String getBytes(Long readBytes){
        //0-表示bytes，1-表示KB，2-MB，3-表示GB，4-表示PB
        int level=0;
        String readBytesStr="";
        double bytes=readBytes;
        while(bytes>100){
            bytes=(bytes/1024);
            level++;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        switch (level){
            case 0:readBytesStr=df.format(bytes)+"B";break;
            case 1:readBytesStr=df.format(bytes)+"KB";break;
            case 2:readBytesStr=df.format(bytes)+"MB";break;
            case 3:readBytesStr=df.format(bytes)+"GB";break;
            case 4:readBytesStr=df.format(bytes)+"PB";break;
            default:readBytesStr=df.format(bytes)+"B";
        }
        return readBytesStr;
    }
}
