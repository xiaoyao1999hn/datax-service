package org.datax.console.base.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 结果返回类
 * Created by ChengJie on 2018/12/7 12:24
 */
@Getter
@Setter
public class ResResult<T> {

    private static final long serialVersionUID = 1L;

    private static final int OK_CODE = 0;

    private static final int ERROR_CODE = 1;

    /**
     * 返回编码
     */
    private Integer code;

    /**
     * 反馈信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public ResResult() {
    }

    public ResResult(int code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.data = t;
    }

    public ResResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResResult ok() {
        return new ResResult(OK_CODE, "成功");
    }

    public static <T> ResResult ok(T t) {
        return new ResResult(OK_CODE, "成功", t);
    }

    public static <T> ResResult ok(int code, String msg) {
        return new ResResult(code, msg);
    }

    public static <T> ResResult ok(String msg, T t) {
        return new ResResult(OK_CODE, msg, t);
    }

    public static <T> ResResult ok(int code, String msg, T t) {
        return new ResResult(code, msg, t);
    }

    public static ResResult ok(String msg) {
        return new ResResult(OK_CODE, msg);
    }

    public static <T> ResResult error() {
        return new ResResult(ERROR_CODE, "失败");
    }

    public static <T> ResResult error(String msg, T t) {
        return new ResResult(ERROR_CODE, msg, t);
    }


    public static <T> ResResult error(String msg) {
        return new ResResult(ERROR_CODE, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
