package com.globalegrow.bigdata.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/22 20:14
 **/
@Slf4j
public enum  HttpUtils {

    INSTANCE;

    /**
     * 执行post请求
     * @param url
     * @param body
     */
    public void doPostRequest(String url,String body){
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.get("application/json"),body))
                .build();
        executeRequest(request);
    }

    /**
     * 创建get请求
     * @return
     */
    public void doGetRequest(String url){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        executeRequest(request);
    }

    /**
     * 执行请求
     * @param request
     */
    public void executeRequest(Request request) {
        try {
            Response response = OkHttpClientUtils.getHttpClient().newCall(request).execute();
            if (response.body() != null) {
                if (response.isSuccessful()) {
                    JSONObject data = JSON.parseObject(response.body().string());
                    if (data.getIntValue("code") != 0) {
                        throw new RuntimeException("failed, resp: " + data.toJSONString());
                    }
                } else {
                    throw new RuntimeException("failed, resp: " + response.body().string());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("execute request exception, cause: " + e.getMessage(), e);
        }
    }
}
