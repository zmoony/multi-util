package com.example.text.demoOnLine.wsdl测试;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * httpclient直接调用
 *
 * @author yuez
 * @since 2023/2/22
 */
public class SoapClient {
    private  String url;
    private  String soapAction = "application/soap+xml";
    private  String xmlRequest;

    public SoapClient(String url, String soapAction, String xmlRequest) {
        this.url = url;
        this.soapAction = soapAction;
        this.xmlRequest = xmlRequest;
    }

    public String execute() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("SOAPAction",soapAction);
        httpPost.setHeader("Content-Type","text/xml;charset=UTF-8");

        StringEntity stringEntity = new StringEntity(xmlRequest, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        if (responseEntity != null) {
            String responseStr = EntityUtils.toString(responseEntity, Charset.forName("utf-8"));
            return responseStr;
        }
        httpClient.close();
        return null;
    }
}
