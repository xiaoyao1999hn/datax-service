package com.globalegrow.bigdata.common.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/10 10:17
 **/
public class ResponeResult {

    public static final Integer OK=0;

    public static final Integer ERROR=1;

    private Integer code;

    private String msg;

    private Map data =new HashMap();

    public static ResponeResult ok(){
        ResponeResult result =new ResponeResult();
        result.setCode(OK);
        result.setMsg("");
        return result;
    }

    public static ResponeResult error(){
        return createResult(ERROR,"服务器异常");
    }

    public static ResponeResult error(Integer code){
        return createResult(code,"服务器异常");
    }

    public static ResponeResult error(Integer code,String msg){
        return createResult(code,msg);
    }

    public static ResponeResult createResult(Integer code,String msg){
        ResponeResult result =new ResponeResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public ResponeResult put(String key,Object value){
        this.data.put(key,value);
        return this;
    }

}
