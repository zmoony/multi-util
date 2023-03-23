package com.boot.util.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片处理工具类
 *
 * @author jmrt
 */
public class ImageUtils
{
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    public static byte[] getImage(String imagePath)
    {
        InputStream is = getFile(imagePath);
        try
        {
            return IOUtils.toByteArray(is);
        }
        catch (Exception e)
        {
            log.error("图片加载异常 {}", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath)
    {
        try
        {
            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        }
        catch (Exception e)
        {
            log.error("获取图片异常 {}", e);
        }
        return null;
    }
 
    /**
     * 读取文件为字节数据
     * 
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url)
    {
        InputStream in = null;
        try
        {
            // 网络地址
            URL urlObj = new URL(url);
            System.out.println(url);
            HttpURLConnection urlConnection = (HttpURLConnection)urlObj.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setReadTimeout(60 * 1000);
            urlConnection.setDoInput(true);
            in = urlConnection.getInputStream();
            byte[] bts = IOUtils.toByteArray(in);
            return bts;
        }
        catch (Exception e)
        {
            log.error("访问文件异常 {}", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }
    
    /**  
     * @Title: streamToImg
     * @Description: 数据流转图片
     * @param input
     * @return BufferedImage
     */
    public static BufferedImage streamToImg(InputStream input)
    {
        try
        {
            return ImageIO.read(input);
        }
        catch (Exception e)
        {
            log.error("图片转换异常", e);
            return null;
        }
    }
}
