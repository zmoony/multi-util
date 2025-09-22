package com.example.text.webTest;

import com.alibaba.excel.util.FileUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 *
 * @author yuez
 * @since 2023/3/22
 */
@RestController
@Log4j2
public class FileController {

    @PostMapping("upload")
    public String fileUpload(MultipartFile file) throws IOException {
        System.out.println(file.getName()+"==="+file.getBytes());
        File destFile = new File("C:\\Users\\yuez\\Desktop\\a.jpg");
        FileUtils.writeToFile(destFile,file.getInputStream());
        return "ok";
    }

    public static void main(String[] args) throws IOException {
        String methodUrl="http://127.0.0.1:9090//upload";
        File destFile = new File("C:\\Users\\yuez\\Desktop\\1679445521171.jpg");
        byte[] bytes = FileUtils.readFileToByteArray(destFile);
        FileInputStream fileInputStream = FileUtils.openInputStream(destFile);
//        HttpEntity entity = MultipartEntityBuilder.create()
//                .addBinaryBody("file",fileInputStream, ContentType.MULTIPART_FORM_DATA,destFile.getName()).build();
//        HttpEntity entity = MultipartEntityBuilder.create()
//                .addBinaryBody("file",bytes, ContentType.MULTIPART_FORM_DATA,destFile.getName()).build();
        //默认 this.addBinaryBody(name, (byte[])b, ContentType.DEFAULT_BINARY, (String)null);
        /* HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("file",bytes, ContentType.MULTIPART_FORM_DATA, LocalDateTime.now().toString()).build();*/

        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("file",bytes, ContentType.IMAGE_JPEG, LocalDateTime.now().toString()).build();
        String result = Httpclient.keyValuePostHead(methodUrl, entity,new HashMap<>());
        System.out.println(result);
    }
}

@Log4j2
class Httpclient{
    public static String  keyValuePostHead(String url , HttpEntity he, Map<String,String> head){
        String content=null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {


            HttpPost httpPost = new HttpPost(url);
            for(Map.Entry<String,String> entry:head.entrySet()){
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

            httpPost.setEntity(he);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
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
}
