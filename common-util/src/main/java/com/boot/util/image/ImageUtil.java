package com.boot.util.image;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author zaiji
 * @date 2020/11/25
 */
@Log4j2
public class ImageUtil {
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
     * @param x
     * @param y
     * @param w
     * @param h
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
}
