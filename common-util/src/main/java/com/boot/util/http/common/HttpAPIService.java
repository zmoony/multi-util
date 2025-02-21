package com.boot.util.http.common;

import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient api封装
 */
@Component
@Log4j2
public class HttpAPIService {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig config;

    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String doGet(String url) throws Exception {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);

        // 装载配置信息
        httpGet.setConfig(config);

        // 发起请求
        try (CloseableHttpResponse response = this.httpClient.execute(httpGet)) {
            // 判断状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 返回响应体的内容
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        }

        return null;
    }

    /**
     * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String doGet(String url, Map<String, Object> map) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);

        if (map != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // 调用不带参数的get请求
        return this.doGet(uriBuilder.build().toString());

    }

    /**
     * 带map参数的post请求
     *
     * @param url
     * @param map
     * @return string json串
     * @throws Exception
     */
    public String doPostByMapParm(String url, Map<String, Object> map) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 加入配置信息
        httpPost.setConfig(config);

        // 判断map是否为空，不为空则进行遍历，封装from表单对象
        if (map != null) {
            List<NameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

            // 把表单放到post里
            httpPost.setEntity(urlEncodedFormEntity);
        }

        // 发起请求
        try(CloseableHttpResponse response = this.httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        }
        return null;
    }

    /**
     * 带json参数的post请求，用的最多，因为map可以转为json
     *
     * @param url 请求地址host+path
     * @param json json字符串
     * @return string json串
     * @throws Exception
     */
    public String doPostByJsonParm(String url, String json) throws Exception {
        //设置请求路径，请求格式，配置信息
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setConfig(config);

        //格式化请求数据并设值
        StringEntity se = new StringEntity(json);
        se.setContentType("text/json");
        httpPost.setEntity(se);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            //返回信息
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        }
        return null;
    }

    /**
     * 不带参数post请求
     *
     * @param url
     * @return string json串
     * @throws Exception
     */
    public String doPost(String url) throws Exception {
        return this.doPostByMapParm(url, null);
    }

    public  String jsonPost(String url, String json) {
        String content = null;
        try {
            //设置请求路径，请求格式，配置信息
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setConfig(config);
            //格式化请求数据并设值
            StringEntity se = new StringEntity(json, "utf-8");
            se.setContentType("text/json");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content = EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        } catch (IOException | UnsupportedCharsetException | ParseException ex) {
            log.error("请求失败" + ex.getMessage());
        }
        return content;

    }
}
