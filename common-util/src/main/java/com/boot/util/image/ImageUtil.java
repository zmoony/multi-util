package com.boot.util.image;

import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author zaiji
 * @date 2020/11/25
 */
@Log4j2
public class ImageUtil {
    static final int VALID_PIC_SIZE = 10;

    /**
     * 判断给定的字符串是否为空。
     *
     * @param picUrl 需要判断的字符串
     * @return 如果字符串为 null、全等 null 字符串、或去除空格后为空字符串则返回 true，否则返回 false
     */
    public static boolean isEmpty(String picUrl) {
        return picUrl == null || "null".equalsIgnoreCase(picUrl)|| picUrl.trim().isEmpty()  ;
    }

    /**
     * 验证指定的图片URL是否有效。
     *
     * @param picUrl 图片的URL地址
     * @return 如果图片URL有效且图片大小大于等于VALID_PIC_SIZE则返回true，否则返回false
     */
    public static boolean isValid(String picUrl) {
        if(isEmpty(picUrl)){
            return false;
        }
        try {
            HttpGet httpGet = new HttpGet(picUrl);
            try (CloseableHttpResponse response = GlobalObject.MyCloseableHttpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                // 检查状态码
                boolean statusCode = statusLine.getStatusCode() == HttpURLConnection.HTTP_OK;
                if (!statusCode) {
                    return false;
                }
                byte[] imageData = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);
                return imageData.length >= VALID_PIC_SIZE;
            }
        } catch (Exception ex) {
            log.error("请求失败",ex);
            return false;
        }
    }
    /**
     *
     * @param url
     * @return
     * @Exception Premature end of Content-Length delimited message body (expected: 2,885,623; received: 2,294,144)
     */
    public static byte[] httpDownloadImage(String url) {
        byte[] content = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            //可用连接池
            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                final InputStream is = response.getEntity().getContent();
                int len = -1;
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                byte[] by = new byte[1024];
                while ((len = is.read(by)) != -1) {
                    data.write(by, 0, len);
                }
                content = data.toByteArray();
            }
        } catch (UnsupportedCharsetException | ParseException | IOException ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;
    }

    /**
     * 压缩图片
     *
     * @param picByte    图片数组
     * @param targetSize 目标大小，单位byte
     */
    public static byte[] compressPicture(byte[] picByte, long targetSize) throws Exception {
        return compressPicture(picByte, targetSize, null, null);
    }

    /**
     * 压缩图片
     *
     * @param picByte    图片数组
     * @param targetSize 目标大小，单位byte
     * @param quality    压缩质量，默认0.5d
     */
    public static byte[] compressPicture(byte[] picByte, long targetSize, Double scale, Double quality) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(picByte); ByteArrayOutputStream output = new ByteArrayOutputStream();) {
            scale = Objects.isNull(scale) ? 0.8d : scale;
            quality = Objects.isNull(quality) ? 0.8d : quality;

            Thumbnails.of(inputStream).scale(scale).outputQuality(quality).outputFormat("jpg").toOutputStream(output);

            byte[] bytes = output.toByteArray();

            if (targetSize >= bytes.length) {
                return bytes;
            }

            return compressPicture(bytes, targetSize, scale - 0.1d, quality - 0.1d);
        }
    }

    /**
     * 裁剪图片
     * 1. createImageInputStream创建图像流
     * 2. 根据图片后缀获取reader对象
     * 3. 设置read的参数，获取bufferImage
     * 4. imageIo.write(bfi,"jpg",out)
     * 5. 获取out的图片流
     * @param data
     * @param x 坐标x
     * @param y 坐标y
     * @param w 宽
     * @param h  高
     */
    public static byte[] cut(byte[] data,int x,int y,int w,int h ){
        try(ByteArrayInputStream in = new ByteArrayInputStream(data);
            ImageInputStream iis = ImageIO.createImageInputStream(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = it.next();
            reader.setInput(iis,true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, w, h);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi,"jpg",out);
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (IOException e) {
            log.error("获取图片错误：{}",e);
        }
        return null;
    }


    /**
     * 对给定的URL进行编码处理。
     * 该方法将输入的URL字符串解析为URL对象，分别获取其协议、主机、端口、路径、查询参数和引用部分。
     * 对路径和查询参数分别调用处理方法进行编码，最后重新构建并返回处理后的URL。
     *
     * @param url 需要编码的原始URL字符串
     * @return 经过编码处理后的URL字符串
     */
    public static String encodeUrl(String url) throws Exception {
        URL urlObj = new URL(url);
        String protocol = urlObj.getProtocol();
        String host = urlObj.getHost();
        int port = urlObj.getPort();
        String path = urlObj.getPath();
        String query = urlObj.getQuery();
        String ref = urlObj.getRef();

        path = handlePath( path);
        query = handleQuery( query);

        // 重新构建URL
        return rebuildUrl(protocol, host, port, path, query, ref);
    }

    /**
     * 处理给定的路径字符串，对其中的各个路径段进行适当的编码修复，并返回处理后的路径。
     *
     * @param path 输入的路径字符串。
     * @return 处理并编码修复后的路径字符串。如果输入路径为 null 或空字符串，则直接返回原始路径。
     */
    private static String handlePath (String path) throws UnsupportedEncodingException {
        if (path != null && !path.isEmpty()) {
            // 将输入路径按斜杠/分割成多个路径段，用于逐个处理每个路径段的编码
            String[] pathSegments = path.split("/");
            StringBuilder encodedPath = new StringBuilder();
            for (int i = 0; i < pathSegments.length; i++) {
                if (!pathSegments[i].isEmpty()) {
                    // 对路径段进行编码
                    String encodedSegment = URLEncoder.encode(pathSegments[i], StandardCharsets.UTF_8);
                    // 修复：还原不应编码的字符
                    // 空格统一为%20
                    encodedSegment = encodedSegment.replace("+", "%20")
                            // 冒号在URL中是合法字符
                            .replace("%3A", ":")
                            // 保持路径分隔符
                            .replace("%2F", "/");
                    encodedPath.append(encodedSegment);
                }
                if (i != pathSegments.length - 1) {
                    encodedPath.append("/");
                }
            }
            return encodedPath.toString();
        }
        return path;
    }

    /**
     * 处理查询参数字符串，对键值对进行编码修复。
     *
     * @param query 查询参数字符串
     * @return 处理后的查询参数字符串
     */
    private static String handleQuery (String query) throws UnsupportedEncodingException {
        // 处理查询参数部分
        if (query != null && !query.isEmpty()) {
            String[] params = query.split("&");
            StringBuilder encodedQuery = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                String[] keyValue = params[i].split("=", 2);
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";

                // 对键值分别编码
                String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);

                // 修复：还原不应编码的字符
                encodedKey = encodedKey.replace("+", "%20").replace("%3A", ":");
                encodedValue = encodedValue.replace("+", "%20").replace("%3A", ":");

                encodedQuery.append(encodedKey).append("=").append(encodedValue);
                if (i != params.length - 1) {
                    encodedQuery.append("&");
                }
            }
            return  encodedQuery.toString();
        }
        return query;
    }

    /**
     * 重建URL字符串。
     *
     * @param protocol 协议部分，例如"http"或"https"
     * @param host 主机名或IP地址
     * @param port 端口号，如果未指定则传入-1
     * @param path 路径部分，例如"/index.html"
     * @param query 查询参数字符串，例如"name=value&name2=value2"，可以为null或空字符串
     * @param ref 锚点部分（片段标识符），例如"section1"，可以为null或空字符串
     * @return 重构后的完整URL字符串
     */
    public static String rebuildUrl(String protocol, String host, int port, String path, String query, String ref) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append("://").append(host);
        if (port != -1) {
            sb.append(":").append(port);
        }
        sb.append(path);
        if (query != null && !query.isEmpty()) {
            sb.append("?").append(query);
        }
        if (ref != null && !ref.isEmpty()) {
            sb.append("#").append(ref);
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        String str = "fdjksfjkdszl-fjsdlfnjdk-nfjldnfj";
        System.out.println(str.replace("-",""));
    }
}
