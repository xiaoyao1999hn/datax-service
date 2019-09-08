package com.globalegrow.bigdata.common.utils;

import okhttp3.OkHttpClient;

import java.time.Duration;

/**
 * User: jiangsongsong
 * Date: 2019/3/8
 * Time: 11:19
 */
public final class OkHttpClientUtils {

    private OkHttpClientUtils() {
    }

    /**
     * get ok http client
     *
     * @return ok http client singleton
     */
    public static OkHttpClient getHttpClient() {
        return InstanceHolder.INSTANCE;
    }


    private static class InstanceHolder {
        private static final OkHttpClient INSTANCE;
        static {
            INSTANCE = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(10))
                .callTimeout(Duration.ofSeconds(15))
                .build();
        }
    }

}
