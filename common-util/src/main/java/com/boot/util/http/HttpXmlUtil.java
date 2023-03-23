package com.boot.util.http;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @author zaiji
 * @date 2020/11/25
 */
@Log4j2
public class HttpXmlUtil {
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final HttpHeaders HEADERS = new HttpHeaders();

    static {
        HEADERS.add("SOAPAction", "application/soap+xml");//根据wsdl文件的soapaction来
        MediaType mediaType = MediaType.parseMediaType("text/xml;charset=UTF-8");
        HEADERS.setContentType(mediaType);
    }

   /* public static String postService(String soapBody, String serviceUrl) {
        HttpEntity<String> request = new HttpEntity<>(soapBody, HEADERS);
        return REST_TEMPLATE.postForObject(serviceUrl, request, String.class);
    }*/

    public static String postService(String soapBody, String serviceUrl) {
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        try{
            httpPost = new HttpPost(serviceUrl);

            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.setHeader("SOAPAction", "application/soap+xml");

            //设置参数
            if(StringUtils.isNotEmpty(soapBody)){
                httpPost.setEntity(new StringEntity(soapBody, Charset.forName("UTF-8")));
            }

            response = HttpUtils.MyCloseableHttpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            String body="";
            if(resEntity != null){
                body = EntityUtils.toString(resEntity,Charset.forName("utf-8"));
            }
            return body;
        }catch(Exception ex){
            log.error("http接口调用失败:"+ex.getMessage());
        }
        return null;
    }
}
