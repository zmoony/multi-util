package com.boot.util.wiscom;

import io.vavr.Tuple2;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
@Component
@Log4j2
public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager connMgr;  
    private static RequestConfig requestConfig;  
    private static final int MAX_TIMEOUT = 7000;  
    
    static {  
        connMgr = new PoolingHttpClientConnectionManager();
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());  
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();  
    }

	public String doGet(String url,Map<String,String> mapHeader,String charset){  
		CloseableHttpClient httpclient = null;

		HttpGet httpGet = null;  
        String result = null;  
        try{
            if(url.startsWith("https")){
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
                        (X509Certificate[] chain, String authType) -> true).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            }else {
                httpclient = HttpClients.createDefault();
            }
            httpGet = new HttpGet(url);  
            
            httpGet.setHeader("Content-Type", "application/json;charset="+charset);
           
            Iterator<String> headers = mapHeader.keySet().iterator();
            
            while(headers.hasNext()){
            	String key = headers.next();
            	
            	String vallue = mapHeader.get(key);
            	
            	httpGet.setHeader(key, vallue);
            }
           
            CloseableHttpResponse response = httpclient.execute(httpGet);   
            HttpEntity resEntity = response.getEntity();  
            if(resEntity != null){  
                result = EntityUtils.toString(resEntity,charset);  
            }  
        }catch(Exception ex){
            log.error("http接口调用失败:"+ex.getMessage());
        }finally {
        	try {
        		httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        return result;  
    }
	

	public Tuple2<StatusLine,String>  doPost(String url, Map<String,String> mapHeader, String jsonBody, String charset)  {
        CloseableHttpResponse response = null;

		HttpPost httpPost = null;
        Tuple2 result = null;
        try{
            /*if(url.startsWith("https")){
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
                        (X509Certificate[] chain, String authType) -> true).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            }else {
                httpclient = HttpClients.createDefault();
            }*/

            httpPost = new HttpPost(url);  
            
            httpPost.setHeader("Content-Type", "application/json;charset="+charset);
           
            Iterator<String> headers = mapHeader.keySet().iterator();
            
            while(headers.hasNext()){
            	String key = headers.next();
            	
            	String vallue = mapHeader.get(key);
            	
            	httpPost.setHeader(key, vallue);
            }
            
            //设置参数
            if(StringUtils.isNotEmpty(jsonBody)){
            	 httpPost.setEntity(new StringEntity(jsonBody, Charset.forName("UTF-8"))); 
            }
           
            response = GlobalObject.MyCloseableHttpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            StatusLine statusLine = response.getStatusLine();
            String body="";
            if(resEntity != null){
                body = EntityUtils.toString(resEntity,charset);
            }
            result = new Tuple2(statusLine,body);
        }catch(Exception ex){
            log.error("http接口调用失败:"+ex.getMessage());
        }finally {
            /*try {
                if (response != null) response.close();
            } catch (IOException e) {
                log.error(e);
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }*/
		}
        
        return result;  
    }

    public static String jsonPost(String url,String json){
        String content=null;
        //String content ="{\"authnom\":\"wiscompsmp101\",\"type\":1,\"content\":\"{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}\"}";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity se = new StringEntity(json, ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            httpPost.setEntity(se);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //如果返回结果是json，返回结果：ResponseEntityProxy{[Content-Type: application/json;charset=UTF-8,Chunked: true]}
//                System.out.println("返回结果："+entity.toString());
//                if(statusLine.getStatusCode()>=300){
//
//                }
//                if (entity == null) {
//
//                }
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content= EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(IOException | UnsupportedCharsetException | ParseException ex){
            log.error("请求失败"+ex.getMessage());
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error("接口httpclient关闭失败:"+ex.getMessage());
            }
        }
        return content;

    }



}


