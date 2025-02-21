/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

/**
 * @author yuez
 * 对于HTTP连接池获取的连接，是否需要手动关闭取决于具体的连接池实现。
 * 大多数成熟的HTTP连接池库会自动管理连接的生命周期，包括回收和重用连接资源。
 * 例如，在Apache HttpClient中，当你从连接池获取一个HttpClient或HttpConnection实例执行请求后，
 * 通常不需要直接调用关闭连接的方法。在完成请求处理后，应当按照连接池的API设计来释放连接回池，而不是关闭它。
 *
 * 对于Apache HttpClient 4.x版本及其以上的连接池实现，通常是通过调用close()或者releaseConnection()方法来将连接返回到连接池中。
 */
@Log4j2
public class HttpClientPoolUtil {
    public static String methodGetFromPool(String url) {
        String content = null;
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpget);
            try {
                // StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content = EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        } catch (UnsupportedCharsetException | ParseException | IOException ex) {
            log.error("", ex);
        }
        return content;

    }

    public static String methodGetHeadFromPool(String url, Map<String, String> head) {

        String content = null;
        try {
            HttpGet httpget = new HttpGet(url);
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpget);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200

                HttpEntity entity = response.getEntity();

                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content = EntityUtils.toString(entity, Charset.forName("utf-8"));
                //log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        } catch (UnsupportedCharsetException | ParseException | IOException ex) {
            log.error(ex.getMessage());
        }
        return content;

    }


    public static String jsonPostHeadFromPool(String url, String json, Map<String, String> head) {
        String content = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

            StringEntity se = new StringEntity(json, ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            httpPost.setEntity(se);
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpPost);
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
                content = EntityUtils.toString(entity, charset);
//                log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        } catch (IOException | UnsupportedCharsetException | ParseException ex) {
            log.error("请求失败" + ex.getMessage());
        }
        return content;

    }

    public static String jsonPutHeadFromPool(String url, String json, Map<String, String> head) {
        String content = null;

        try {
            HttpPut httpput = new HttpPut(url);
            httpput.setHeader("Content-Type", "application/json;charset=UTF-8");

            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpput.addHeader(entry.getKey(), entry.getValue());
            }
            StringEntity se = new StringEntity(json, ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            httpput.setEntity(se);
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpput);
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

    public static String jsonPatchHeadFromPool(String url, String json, Map<String, String> head) {
        String content = null;

        try {
            HttpPatch httpPatch = new HttpPatch(url);
            httpPatch.setHeader("Content-Type", "application/json;charset=UTF-8");

            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpPatch.addHeader(entry.getKey(), entry.getValue());
            }
            StringEntity se = new StringEntity(json, ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            httpPatch.setEntity(se);
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpPatch);
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

    public static String methodDeleteFromPool(String url, Map<String, String> head) {
        String content = null;

        try {
            HttpDelete httpdelete = new HttpDelete(url);
            httpdelete.setHeader("Content-Type", "application/json;charset=UTF-8");
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpdelete.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpdelete);
            try {
                HttpEntity entity = response.getEntity();
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content = EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        } catch (UnsupportedCharsetException | ParseException | IOException ex) {
            log.error(ex.getMessage());
        }
        return content;
    }


    public static byte[] httpDownloadImageFromPool(String url) {
        byte[] content = null;
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpget);
            try {
                // StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        } catch (Exception ex) {
            log.error("下载图片异常：" + ex.getMessage() + " 图片路径：" + url);
        }
//        finally {
//            try {
//                httpclient.close();
//            } catch (IOException ex) {
//                log.error(ex.getMessage());
//            }
//        }
        return content;

    }

    public static String jsonPostFromPool(String url, String json) {
        String content = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity se = new StringEntity(json, ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            httpPost.setEntity(se);
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpPost);
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
                content = EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        } catch (IOException | UnsupportedCharsetException | ParseException ex) {
            log.error("请求失败" + ex.getMessage());
        }
//        finally {
//            try {
//                httpclient.close();
//            } catch (IOException ex) {
//                log.error("接口httpclient关闭失败:"+ex.getMessage());
//            }
//        }
        return content;

    }




    public static String keyValuePostFromPool(String url, HttpEntity he) {
        String content = null;
        try {
            HttpPost httpPost = new HttpPost(url);
//            List <NameValuePair> nvps = new ArrayList <>();
//            nvps.add(new BasicNameValuePair("authnom", "wiscompsmp101"));
//            nvps.add(new BasicNameValuePair("type", String.valueOf(1)));
//            nvps.add(new BasicNameValuePair("content", "{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}"));            
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));


            httpPost.setEntity(he);

            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpPost);
            try {
                //StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200 
                HttpEntity entity = response.getEntity();
                //如果返回结果是json，返回结果：ResponseEntityProxy{[Content-Type: application/json;charset=UTF-8,Chunked: true]}
//                System.out.println("返回结果："+entity.toString()); 
//                if(statusLine.getStatusCode()>=300){
//                    return false;
//                }
//                if (entity == null) {
//                    return false;
//                }
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content = EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                return content;

            } finally {
                response.close();
            }


        } catch (IOException | UnsupportedCharsetException | ParseException ex) {
            log.error("接口失败" + ex.getMessage());

        }
//        finally {
//            try {
//                httpclient.close();
//            } catch (IOException ex) {
//                log.error("接口httpclient关闭失败:"+ex.getMessage());
//            }
//        }
        return content;
    }


    public static byte[] getPicByte(String imgUrl, int retryTimes) {
        byte[] content = null;
        try {
            HttpGet httpget = new HttpGet(imgUrl);
            CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
            if (content == null || content.length < 1000) {
                if (retryTimes > 0) {
                    retryTimes--;
                    log.info("重试,剩余"+retryTimes+"次"+imgUrl);
                    content = getPicByte(imgUrl, retryTimes);
                }
            }
        } catch (Exception ex) {
            log.error("下载图片异常：" + ex + " 图片路径：" + imgUrl);
            if (retryTimes > 0) {
                retryTimes--;
                log.info("重试,剩余"+retryTimes+"次"+imgUrl);
                content = getPicByte(imgUrl, retryTimes);
            }
        }
        return content;
    }


}
