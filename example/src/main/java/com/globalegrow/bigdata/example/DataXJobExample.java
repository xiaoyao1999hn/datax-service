package com.globalegrow.bigdata.example;

import okhttp3.*;
import java.io.*;

/**
 * @author ：Chengjie
 * @date ：Created in 2019/9/8 21:53
 * @description：
 */
public class DataXJobExample {

    public static void main(String[] args) throws IOException {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

        String url ="http://localhost:21000/datax/job/start";
        String json=loadJson();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
       if (response.isSuccessful()) {
            System.out.print(response.body().string());
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }



    /**
     * 通过本地文件访问json并读取
     * @return：json文件的内容
     */
    public static String loadJson(){
        StringBuffer result=new StringBuffer();
        // 打开文件
        BufferedReader reader=null;
        try{
            // 读取文件
            reader=new BufferedReader(new InputStreamReader(DataXJobExample.class.getResourceAsStream("job.json"),"UTF-8"));
            String tempString=null;
            while((tempString=reader.readLine())!=null){
                result.append(tempString);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader!=null){
                try{
                    reader.close();
                }catch(IOException el){
                }
            }
        }
        return result.toString();
    }

}
