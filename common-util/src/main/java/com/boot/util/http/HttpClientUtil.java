package com.boot.util.http;

import com.boot.util.mapUtil.ObjectMapperUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Component
@Log4j2
public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;

//    3.1 after
//    public void init() {
//        connectionManager = new MultiThreadedHttpConnectionManager();
//        HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
//        managerParams.setMaxTotalConnections(500); // 最大连接数
//        .setDefaultMaxConnectionsPerHost //需要设置最大连接主机，否则默认是2,会引起大量的线程等待
//        connectionManager.setParams(managerParams);
//        client = new HttpClient(connectionManager);
//    }
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

    public String doGet(String url, Map<String, String> mapHeader, String charset) {
        CloseableHttpClient httpclient = null;

        HttpGet httpGet = null;
        String result = null;
        try {
            if (url.startsWith("https")) {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
                        (X509Certificate[] chain, String authType) -> true).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            } else {
                httpclient = HttpClients.custom()
                        .setConnectionManager(connMgr)
                        .setDefaultRequestConfig(requestConfig)
                        .build();
            }
            httpGet = new HttpGet(url);

            httpGet.setHeader("Content-Type", "application/json;charset=" + charset);

            Iterator<String> headers = mapHeader.keySet().iterator();

            while (headers.hasNext()) {
                String key = headers.next();

                String vallue = mapHeader.get(key);

                httpGet.setHeader(key, vallue);
            }

            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        } catch (Exception ex) {
            log.error("http接口调用失败:" + ex.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * @param url      post 同步请求路径
     * @param jsonBody 请求体json格式数据
     * @param charset
     * @return
     */
    public String doPost(String url, Map<String, String> mapHeader, String jsonBody, String charset) {
        CloseableHttpClient httpclient = null;
        if (url.startsWith("https")) {
            httpclient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpclient = HttpClients.createDefault();
        }

        HttpPost httpPost = null;
        String result = null;
        try {
            httpPost = new HttpPost(url);

            httpPost.setHeader("Content-Type", "application/json;charset=" + charset);

            Iterator<String> headers = mapHeader.keySet().iterator();

            while (headers.hasNext()) {
                String key = headers.next();

                String vallue = mapHeader.get(key);

                httpPost.setHeader(key, vallue);
            }

            //设置参数
            if (StringUtils.isNotEmpty(jsonBody)) {
                httpPost.setEntity(new StringEntity(jsonBody, Charset.forName("UTF-8")));
            }

            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
            EntityUtils.consume(resEntity);  //确保响应体消费完成
        } catch (Exception ex) {
            log.error(String.format("http请求出错:%s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error(String.format("http请求出错:%s", e.getMessage()));
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 非json参数方式POST提交
     *
     * @param uri,params
     * @param params
     * @return
     */
    public static String httpPost(String uri, List<NameValuePair> params) {
        String result = "";
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded"); // 添加请求头
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            System.out.println(httpPost);
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            log.error(String.format("http请求出错:{}", e));
        }
        return result;
    }

    /**
     * @param url     post 同步请求路径
     * @param StrXML  请求体xml格式数据
     * @param charset
     * @return
     */
    public String doPostXML(String url, Map<String, String> mapHeader, String StrXML, String charset) {
        CloseableHttpClient httpclient = null;
        if (url.startsWith("https")) {
            httpclient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpclient = HttpClients.createDefault();
        }

        HttpPost httpPost = null;
        String result = null;
        try {
            httpPost = new HttpPost(url);

            httpPost.setHeader("Content-Type", "application/json;charset=" + charset);

            Iterator<String> headers = mapHeader.keySet().iterator();

            while (headers.hasNext()) {
                String key = headers.next();

                String vallue = mapHeader.get(key);

                httpPost.setHeader(key, vallue);
            }

            //设置参数
            if (StringUtils.isNotEmpty(StrXML)) {
                httpPost.setEntity(new StringEntity(StrXML, Charset.forName("UTF-8")));
            }

            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public Document XMLBody(String xmlStr) {
        Document doc = null;
        StringReader sr = new StringReader(xmlStr);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * @param url      post 同步请求路径
     * @param jsonBody 请求体json格式数据
     * @param charset
     * @return 有可能会导致close wait
     */
    public String doAsyncPost(String url, Map<String, String> mapHeader, String jsonBody, String charset) {
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient.start();
            httpPost = new HttpPost(url);

            httpPost.setHeader("Content-Type", "application/json;charset=" + charset);

            Iterator<String> headers = mapHeader.keySet().iterator();

            while (headers.hasNext()) {
                String key = headers.next();

                String value = mapHeader.get(key);

                httpPost.setHeader(key, value);
            }

            //设置参数
            httpPost.setEntity(new StringEntity(jsonBody, Charset.forName("UTF-8")));

            Future<HttpResponse> future = httpClient.execute(httpPost, null);
            HttpResponse response = future.get();
            log.info(response.getStatusLine());
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                log.info("resEntity:" + resEntity);
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * @param url      post 异步请求路径
     * @param jsonBody 请求体json格式数据
     * @param charset
     * @return
     */
    public String doDelete(String url, Map<String, String> mapHeader, String jsonBody, String charset) {
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = null;
        if (url.startsWith("https")) {
            httpclient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpclient = HttpClients.createDefault();
        }
        HttpDeleteWithBody httpDelete = null;
        String result = null;
        try {
            httpDelete = new HttpDeleteWithBody(url);

            httpDelete.setHeader("Content-Type", "application/json;charset=" + charset);

            Iterator<String> headers = mapHeader.keySet().iterator();

            while (headers.hasNext()) {
                String key = headers.next();

                String vallue = mapHeader.get(key);

                httpDelete.setHeader(key, vallue);
            }

            //设置参数
            if (StringUtils.isNotEmpty(jsonBody)) {
                httpDelete.setEntity(new StringEntity(jsonBody, Charset.forName("UTF-8")));
            }

            CloseableHttpResponse response = httpclient.execute(httpDelete);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * @param url      post 异步请求路径
     * @param jsonBody 请求体json格式数据
     * @param charset
     * @return
     */
    public String doAsyncDelete(String url, Map<String, String> mapHeader, String jsonBody, String charset) {
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();

        HttpDeleteWithBody httpDelete = null;
        String result = null;
        try {
            httpClient.start();
            httpDelete = new HttpDeleteWithBody(url);

            httpDelete.setHeader("Content-Type", "application/json;charset=" + charset);

            Iterator<String> headers = mapHeader.keySet().iterator();

            while (headers.hasNext()) {
                String key = headers.next();

                String vallue = mapHeader.get(key);

                httpDelete.setHeader(key, vallue);
            }

            //设置参数
            httpDelete.setEntity(new StringEntity(jsonBody, Charset.forName("UTF-8")));

            Future<HttpResponse> future = httpClient.execute(httpDelete, null);
            HttpResponse response = future.get();
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }


    /**
     * Json请求(表单)
     * @param strAddr 请求地址
     * @param strJson 请求内容(json格式)
     * @return String 响应内容
     */
    public static String jsonRequestForm(String strAddr, String strJson) {
        String strRet = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost httpPost = getHttPostForm(strAddr, strJson);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            strRet = getResponseContent(response);
        } catch (IOException | UnsupportedCharsetException | ParseException ex) {
            log.error("请求失败" + ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException ex) {
                log.error("接口HttpClient关闭失败:" + ex.getMessage());
            }
        }
        return strRet;
    }

    /**
     * 获取HttpPost发送对象（表单）
     * @param strUrl 请求url
     * @param strJson 请求参数（json格式）
     * @return HttpPost 请求体
     */
    private static HttpPost getHttPostForm(String strUrl, String strJson) throws Exception {
        HttpPost httpPost = new HttpPost(strUrl);
        Map<String,Object> mapJson = ObjectMapperUtil.json2map(strJson);

        //设置请求参数
        List<NameValuePair> data = new ArrayList<>();
        for (String key : mapJson.keySet()) {
            data.add(new BasicNameValuePair(key,String.valueOf(mapJson.get(key))));
        }
        // UrlEncodedFormEntity类创建的对象可以模拟传统的HTML表单传送POST请求中的参数
        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data);
            httpPost.setEntity(formEntity);
        } catch (Exception e) {
            log.warn("获取http post失败" + e);
        }

        return httpPost;
    }

    /**
     * 获取Http返回内容
     * @param response 响应
     * @return String 响应内容
     */
    private static String getResponseContent(CloseableHttpResponse response) {
        String strRet;
        try {
            try {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                //获取内容
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                String content = EntityUtils.toString(entity, charset);
                log.debug("响应体中消息内容:" + content);
                strRet = content;
                //确保响应体消费完成
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (IOException | UnsupportedCharsetException | ParseException ex) {
            log.error("请求失败" + ex.getMessage());
            strRet = ex.getMessage();
        }
        return strRet;
    }
}

class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody() {
        super();
    }
}



