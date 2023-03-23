package com.boot.util.http;

import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp
 *
 * @author yuez
 * @since 2022/9/5
 */
@Log4j2
public class OkHttp {
    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .build();

    public static String get(String url, Map<String, String> headerMap) throws Exception {
        Request.Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headerMap)) {
            headerMap.forEach((name, value) -> builder.addHeader(name, value));
        }
        Request request = builder.build();
        ResponseBody body = client.newCall(request).execute().body();
        return body.string();
    }

    public static String getResult(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String string = response.body().string();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String jsonPostHead(String url, String json, Map<String,String> head){
        String content=null;
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json);
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .method("POST", body);
            for (Map.Entry<String, String> entry : head.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
            builder.addHeader("Content-Type", "application/json");
            Request request = builder.build();
            log.info("request:{},head:{}",request.body().toString(),request.headers().toString());
            Response response = client.newCall(request).execute();
            try {
                content = response.body().string();
            } finally {
                response.close();
            }
        }catch(Exception ex){
            log.error("请求失败:{}",ex);
        }
        return content;

    }

    public static String putData(String url, String reqbody, Map<String, String> map) {
        RequestBody requestBody = RequestBody.create(reqbody,MediaType.parse("application/json; charset=utf-8"));
        Request.Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(map)) {
            map.forEach((name, value) -> builder.addHeader(name, value));
        }
        Request request = builder.put(requestBody).build();
        try {
            ResponseBody body = client.newCall(request).execute().body();
            return resolver(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String deleteData(String url, String reqbody, Map<String, String> map) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(map)) {
            map.forEach((name, value) -> builder.addHeader(name, value));
        }
        if(StringUtils.isNotEmpty(reqbody)){
            builder.delete(RequestBody.create(reqbody,MediaType.parse("application/json; charset=utf-8")));
        }else{
            builder.delete();
        }
        Call call = okHttpClient.newCall(builder.build());
        try {
            Response response = call.execute();
            String string = response.body().string();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String resolver(ResponseBody responseBody) {
        InputStream is = null;
        String result = null;
        try {
            is = responseBody.byteStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String body = null;
            StringBuilder sb = new StringBuilder();
            while ((body = br.readLine()) != null) {
                sb.append(body);
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }












}
