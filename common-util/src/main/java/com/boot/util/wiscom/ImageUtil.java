package com.boot.util.wiscom;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * 图片功能
 *
 * @author hwang
 * @date 2022-04-20
 */
@Log4j2
public class ImageUtil {

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
            scale = Objects.isNull(scale) ? 0.6d : scale;
            quality = Objects.isNull(quality) ? 0.6d : quality;

            Thumbnails.of(inputStream).scale(scale).outputQuality(quality).outputFormat("jpg").toOutputStream(output);

            byte[] bytes = output.toByteArray();

            if (targetSize >= bytes.length) {
                return bytes;
            }

            return compressPicture(bytes, targetSize, scale - 0.05, quality - 0.1d);
        }
    }

    /**
     * 下载图片
     *
     * @param imageUrl 图片url
     * @return String 图片base64字符串
     */
    public static String getByteFromUrl(String imageUrl) {
        byte[] btImg = null;
        InputStream inStream = null;
        try {
            imageUrl = imageUrl.replaceAll(" ", "%20");
            imageUrl = imageUrl.replaceAll("\\\\+", "/");
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(300 * 1000);
            // 通过输入流获取图片数据
            inStream = conn.getInputStream();
            // 得到图片的二进制数据
            btImg = readInputStream(inStream);
        } catch (IOException e) {
            log.warn("从{}下载图片发生异常", imageUrl);
            log.error("", e);
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    log.error("图片流关闭发生异常", e);
                }
            }
        }
        return null == btImg ? null : Base64Utils.encodeToString(btImg);
    }

    /**
     * 图片流读取
     *
     * @param inStream 输入流
     * @return byte[] 图片字节流
     */
    public static byte[] readInputStream(InputStream inStream) {
        int len = 0;
        byte[] buffer = new byte[1024];
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            return outStream.toByteArray();
        } catch (IOException e) {
            log.error("读取图片流发生异常", e);
        }

        return null;
    }

    /**
     * 存储图片返回uri
     *
     * @param image 图片流
     * @return 图片uri
     */
    public static String storeFile(byte[] image,String fileSuffix,String headPath,String bottomPath,String httpAddr) {
        if (null == image || StringUtil.isBlank(fileSuffix) ||
                StringUtil.isBlank(headPath) || StringUtil.isBlank(httpAddr)) {
            return "error";
        }
        //调用filestore
        Map<String, Object> data = new HashMap<>(8);
        data.put("pass_time", DateToString.getCurrentDateTime());
        data.put("content", image);
        data.put("file_suffix", fileSuffix);
        data.put("head_path", headPath);
        data.put("bottom_path", bottomPath);
        String strUrl = HttpClientUtil.jsonPost(httpAddr, ObjectToJsonUtil.objectToJson(data));
        return StringUtil.isBlank(strUrl) ? "" : strUrl;
    }

    /**
     * 图片合成
     *
     * @param title    标题文字
     * @param imgInfos 图片信息列表
     * @return byte[] 合成后图片字节流
     */
    public static byte[] compound(String title, List<ImgInfo> imgInfos) {
        if (null == imgInfos || imgInfos.isEmpty()) {
            return null;
        }
        List<ImgInfo> bufImageInfos = new ArrayList<>();
        for (ImgInfo imgInfo : imgInfos) {
            BufferedImage image = getImgFromBytes(imgInfo.getPicBts());
            if (null != image) {
                imgInfo.setImg(image);
                bufImageInfos.add(imgInfo);
            }
        }
        BufferedImage tagImg = compoundImpl(title, bufImageInfos);
        return getByteFromImg(tagImg);
    }

    /**
     * 读取图片变成字节流
     *
     * @param img 图片缓存
     * @return byte[] 图片字节流
     */
    private static byte[] getByteFromImg(BufferedImage img) {
        byte[] imageInByte = null;
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            ImageIO.write(img, "jpg", bout);
            bout.flush();
            //使用toByteArray()方法转换成字节数组
            imageInByte = bout.toByteArray();
        } catch (Exception e) {
            log.error("从图片读取字节流异常：", e);
        }
        return imageInByte;
    }

    /**
     * 合成图片
     *
     * @param title    文字标题
     * @param imgInfos 图片列表
     * @return BufferedImage 合成后图片缓存
     */
    private static BufferedImage compoundImpl(String title, List<ImgInfo> imgInfos) {
        BufferedImage img = imgInfos.get(0).getImg();
        int width = imgInfos.stream().map(ImgInfo::getImg).mapToInt(BufferedImage::getWidth).sum();
        int height = img.getHeight();
        int totalHeight = height + 73 * StringUtil.size(title, width);

        BufferedImage tagImg = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g2d = tagImg.getGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, totalHeight);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("宋体", Font.PLAIN, 63));
        int x = 0, y = 0;
        for (ImgInfo imgInfo : imgInfos) {
            width = imgInfo.getImg().getWidth();
            //            g2d.drawString(imgInfo.getText(), x, 120);
            // 上面的文字取消，此处变为120，如果不取消，则为150适宜
            g2d.drawImage(imgInfo.getImg(), x, y, width, height, null);
            x += width;
        }

        String temp;
        int index, fontHeight = 0;
        do {
            fontHeight += 63;
            index = StringUtil.findIndex(title, width);
            temp = title.substring(0, index);
            g2d.drawString(temp, 0, height + fontHeight);
            title = title.substring(index);
        } while (title.length() > 0);

        return tagImg;
    }

    /**
     * 图片添加水印
     *
     * @param bufImg           图片缓存
     * @param waterMarkContent 水印文字内容
     * @return byte[] 图片字节流
     */
    private static byte[] addWaterMark(BufferedImage bufImg, String waterMarkContent) {

        try {
            // 加水印
            int srcImgWidth = bufImg.getWidth();
            int srcImgHeight = bufImg.getHeight();
            BufferedImage tagImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = tagImg.createGraphics();
            g.drawImage(bufImg.getSubimage(0, 0, srcImgWidth, srcImgHeight), 0, 0, srcImgWidth, srcImgHeight, null);
            //根据图片的背景设置水印颜色
            g.setColor(new Color(25, 25, 25, 255));
            //设置字体
            g.setFont(new Font("微软雅黑", Font.PLAIN, 25));

            int x = 45;
            int y = 45;
            //画出水印
            g.drawString(waterMarkContent, x, y);
            g.dispose();

            return getByteFromImg(tagImg);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("添加水印失败：{}", e.getMessage());
        }
        // 输出图片
        return null;
    }

    /**
     * 保存图片文件到本地
     *
     * @param tagImg 图片缓存
     * @param path   保存路径 例如：./config/1.jpg
     */
    private static void saveFile(BufferedImage tagImg, String path) {
        try {
            FileOutputStream outImgStream = new FileOutputStream(path);
            ImageIO.write(tagImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            log.error("保存文件失败：{}", e.getMessage());
        }
    }

    /**
     * 将字节流转换成图片缓存
     *
     * @param pic 图片字节流
     * @return BufferedImage 图片缓存
     */
    private static BufferedImage getImgFromBytes(byte[] pic) {
        if (null == pic) {
            return null;
        }

        BufferedImage img = null;
        try (ByteArrayInputStream bin = new ByteArrayInputStream(pic)) {
            img = ImageIO.read(bin);
        } catch (Exception e) {
            log.error("读取图片数据异常", e);
        }

        return img;
    }



    public static String storePic(byte[] bytes,String pass_time,String address){
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("content", bytes);
        imageMap.put("pass_time", pass_time);
        imageMap.put("file_suffix", "jpg");
        imageMap.put("head_path", "face,md,image");
        return HttpClientUtil.jsonPost(address, ObjectToJsonUtil.objectToJson(imageMap));
    }

    /**
     * @param bytes
     * @param path
     * @return
     */
    public static String creatLocalImage(byte[] bytes, String path) {
        String dirPath = GlobalObject.properties_business_my.getProperty("ztc.image").trim();
        File file = new File(dirPath + File.separator + path);
        //存放图片的目录路径
        String realurl = null;
        if (!file.exists()) {
            file.mkdirs();
        }
        OutputStream os = null;
        InputStream image_is = null;
        try {
            image_is = new ByteArrayInputStream(bytes);
            String filename = UUID.randomUUID().toString() + ".jpg";
            os = new FileOutputStream(dirPath + File.separator + path + File.separator + filename);
            //将输入流复制到输出流中
            IOUtils.copy(image_is, os);
            os.flush();
            realurl = path + File.separator + filename;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("存储图片发生异常："+ex);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (image_is != null) {
                    image_is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return realurl;

    }

    public static String image2Base64(String imgUrl) {
//		log.info("图片下载开始："+imgUrl);
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestMethod("GET");
            httpUrl.setConnectTimeout(5000);
            httpUrl.setReadTimeout(30000);
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();

            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int len = -1;

            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
//			log.info("图片下载完成："+imgUrl);
            return Base64Util.encode(outStream.toByteArray());
        } catch (Exception e) {
            log.error("image to base64 error ," + e, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("io exception," + e, e);
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return imgUrl;
    }
}
@Data
class ImgInfo
{
    /** 说明文字*/
    private String text;

    /** 图片字节流*/
    private byte[] picBts;

    /** 图片*/
    private BufferedImage img;

    /**
     * <p>Title: 构造函数</p>
     * <p>Description: </p>
     */
    public ImgInfo(String text, byte[] picBts)
    {
        this.text = text;
        this.picBts = picBts;
    }
}