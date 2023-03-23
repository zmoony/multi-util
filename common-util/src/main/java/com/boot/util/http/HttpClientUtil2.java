/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.http;


import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

import static com.boot.util.http.HttpUtils.MyCloseableHttpClient;

/**
 *
 * @author xxzhou
 */
@Log4j2
public class HttpClientUtil2 {
    public static String  methodGet(String url) {
        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
               // StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content= EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(UnsupportedCharsetException | ParseException  | IOException ex){
            log.error(ex.getMessage());
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;
    
    }
    public static String  methodGetHead(String url,Map<String,String> head) {

        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
          
            for(Map.Entry<String,String> entry:head.entrySet()){
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
            

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200

                HttpEntity entity = response.getEntity();
         
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content= EntityUtils.toString(entity, Charset.forName("utf-8"));
                //log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(UnsupportedCharsetException | ParseException  | IOException ex){
            log.error(ex.getMessage());
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;
    
    }
    public static String jsonPostHead(String url,String json,Map<String,String> head){
        String content=null;
        //String content ="{\"authnom\":\"wiscompsmp101\",\"type\":1,\"content\":\"{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}\"}";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            
            for(Map.Entry<String,String> entry:head.entrySet()){
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            
            StringEntity se = new StringEntity(json,ContentType.create("text/json", "UTF-8"));
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
//                log.info("响应体中消息内容:"+content);
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

    public static String jsonPutHead(String url,String json,Map<String,String> head){
        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPut httpPost = new HttpPut(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

            for(Map.Entry<String,String> entry:head.entrySet()){
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            StringEntity se = new StringEntity(json,ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            httpPost.setEntity(se);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content= EntityUtils.toString(entity, charset);
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

    public static String  methodDelete(String url,Map<String,String> head) {
        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpDelete httpdelete = new HttpDelete(url);
            httpdelete.setHeader("Content-Type", "application/json;charset=UTF-8");
            for(Map.Entry<String,String> entry:head.entrySet()){
                httpdelete.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response = httpclient.execute(httpdelete);
            try {
                HttpEntity entity = response.getEntity();
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content= EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(UnsupportedCharsetException | ParseException  | IOException ex){
            log.error(ex.getMessage());
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;
    }
    
    public static String  methodDelete(String url) {
        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            
            HttpDelete httpdelete = new HttpDelete(url);
            CloseableHttpResponse response = httpclient.execute(httpdelete);
            try {
               // StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content= EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(UnsupportedCharsetException | ParseException  | IOException ex){
            log.error(ex.getMessage());
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;
    
    }
    
    public static byte[]  httpDownloadImage(String url) {
        byte[] content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
               // StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                content= EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(Exception ex){
            log.error("下载图片异常："+ex.getMessage()+" 图片路径："+url);
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;
    
    }
    
    
    public static byte[]  httpDownloadImage(String url,Map<String,String> head) {
        byte[] content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            for(Map.Entry<String,String> entry:head.entrySet()){
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
               // StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                content= EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(Exception ex){
            log.error("下载图片异常："+ex.getMessage()+" 图片路径："+url);
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;
    
    }
    
    public static byte[]  httpDownloadImageFromPool(String url) {
        byte[] content=null;
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = MyCloseableHttpClient.execute(httpget);
            try {
               // StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                content= EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);  //确保响应体消费完成
            } finally {
                response.close();
            }
        }catch(Exception ex){
            log.error("下载图片异常："+ex.getMessage()+" 图片路径："+url);
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
    
    public static String jsonPost(String url,String json){
        String content=null;
        //String content ="{\"authnom\":\"wiscompsmp101\",\"type\":1,\"content\":\"{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}\"}";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity se = new StringEntity(json,ContentType.create("text/json", "UTF-8"));
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
                log.info("响应体中消息内容:"+content);
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
    public static String jsonPostFromPool(String url,String json){
        String content=null;
        //String content ="{\"authnom\":\"wiscompsmp101\",\"type\":1,\"content\":\"{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}\"}";
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity se = new StringEntity(json,ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            httpPost.setEntity(se);
            CloseableHttpResponse response = MyCloseableHttpClient.execute(httpPost);
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
    public static String  keyValuePost(String url ,HttpEntity he){
        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            
            
            HttpPost httpPost = new HttpPost(url);
//            List <NameValuePair> nvps = new ArrayList <>();
//            nvps.add(new BasicNameValuePair("authnom", "wiscompsmp101"));
//            nvps.add(new BasicNameValuePair("type", String.valueOf(1)));
//            nvps.add(new BasicNameValuePair("content", "{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}"));            
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            
            
            httpPost.setEntity(he);
            
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200 
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
                content= EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                return content;
                
            } finally {
                response.close();
            }
            
        
        }catch(IOException | UnsupportedCharsetException | ParseException ex){
            log.error("接口失败"+ex.getMessage());
        
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error("接口httpclient关闭失败:"+ex.getMessage());
            }
        }
        return content;
    }
    
    public static String  keyValuePostHead(String url ,HttpEntity he,Map<String,String> head){
        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            
            
            HttpPost httpPost = new HttpPost(url);
//            List <NameValuePair> nvps = new ArrayList <>();
//            nvps.add(new BasicNameValuePair("authnom", "wiscompsmp101"));
//            nvps.add(new BasicNameValuePair("type", String.valueOf(1)));
//            nvps.add(new BasicNameValuePair("content", "{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}"));            
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            for(Map.Entry<String,String> entry:head.entrySet()){
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            
            httpPost.setEntity(he);
            
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200 
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
                content= EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                return content;
                
            } finally {
                response.close();
            }
            
        
        }catch(IOException | UnsupportedCharsetException | ParseException ex){
            log.error("接口失败"+ex.getMessage());
        
        }finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error("接口httpclient关闭失败:"+ex.getMessage());
            }
        }
        return content;
    }
    
    public static String  keyValuePostFromPool(String url ,HttpEntity he){
        String content=null;
        try {
            
            
            HttpPost httpPost = new HttpPost(url);
//            List <NameValuePair> nvps = new ArrayList <>();
//            nvps.add(new BasicNameValuePair("authnom", "wiscompsmp101"));
//            nvps.add(new BasicNameValuePair("type", String.valueOf(1)));
//            nvps.add(new BasicNameValuePair("content", "{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}"));            
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            
            
            httpPost.setEntity(he);
            
            CloseableHttpResponse response = MyCloseableHttpClient.execute(httpPost);
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
                content= EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                return content;
                
            } finally {
                response.close();
            }
            
        
        }catch(IOException | UnsupportedCharsetException | ParseException ex){
            log.error("接口失败"+ex.getMessage());
        
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
    
    
    public static String yituAddPicRquest(String json,String url,String session){
    String res=null;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    int session_time_out=Integer.parseInt("6000");
    try {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setHeader("session_id",session);
        StringEntity se = new StringEntity(json,ContentType.create("text/json", "UTF-8"));
        httpPost.setEntity(se);
        RequestConfig requestConfig=RequestConfig.custom().setSocketTimeout(session_time_out).setConnectTimeout(10000).build();

        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
            HttpEntity entity = response.getEntity();
            //获取
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            res= EntityUtils.toString(entity, charset);
            //log.info("响应体中消息内容:"+res);
            EntityUtils.consume(entity);  //确保响应体消费完成
        } finally {
            response.close();
        }
    }catch(IOException | UnsupportedCharsetException | ParseException ex){
        log.error("请求失败"+ex.getMessage());
        return null;
    }finally {
        try {
            httpclient.close();
        } catch (IOException ex) {
            log.error("接口httpclient关闭失败:"+ex.getMessage());
        }
    }
    return res;

}

    public static boolean jsonRequest(String url,String json){
        boolean flag=false;
        //String content ="{\"authnom\":\"wiscompsmp101\",\"type\":1,\"content\":\"{\"sbbh\":\"001\",\"cdbh\":\"2\",\"jgsk\":\"20180606121456789\",\"hpys\":\"蓝\",\"hphm\":\"苏A9G7X0\",\"txmc\":\"http://172.17.112.129/kk01/test1.jpg\",\"hpzl\":\"02\"}\"}";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        int session_time_out=Integer.parseInt("6000");
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity se = new StringEntity(json,ContentType.create("text/json", "UTF-8"));
            se.setContentType("text/json");
            RequestConfig requestConfig=RequestConfig.custom().setSocketTimeout(session_time_out).setConnectTimeout(10000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(se);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //如果返回结果是json，返回结果：ResponseEntityProxy{[Content-Type: application/json;charset=UTF-8,Chunked: true]}
                //System.out.println("返回结果："+entity.toString());
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                String content= EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                if(content.contains("ok")){
                    flag=true;
                }
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
        return flag;

    }

    /*public static boolean sendData(List<Map<String, Object>> list) {
        com.wiscom.face.bean.RequestBean requestBean = new com.wiscom.face.bean.RequestBean();
        requestBean.setAuthnom(GlobalObject.properties_yitu.getProperty("kafka.http.authnom"));
        requestBean.setType(Integer.parseInt(GlobalObject.properties_yitu.getProperty("kafka.http.type").trim()));
        requestBean.setContent(list);
        String json = ObjectToJson.objectToJson(requestBean);
        return HttpClientUtil.jsonRequest(GlobalObject.properties_yitu.getProperty("kafka.http.address"),json);
    }*/



}
