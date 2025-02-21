package com.boot.util.wiscom.存储服务.http;


import com.boot.util.wiscom.HttpClientPoolUtil;
import com.boot.util.wiscom.ObjectToJsonUtil;
import com.boot.util.wiscom.存储服务.bean.FileStoreBean;
import com.boot.util.wiscom.存储服务.bean.FileStoreConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片工具
 *
 * @author yuez
 * @since 2023/2/6
 */
public class PicUtil {
    public static  String filestore(FileStoreBean fileStoreBean, FileStoreConfig fileStoreConfig){
        String filestoreUrl = fileStoreConfig.getFileStoreUrl();
        Map<String, Object> imageParam = new HashMap<>();
        imageParam.put("file_suffix", fileStoreBean.getFile_suffix());
        imageParam.put("pass_time", fileStoreBean.getPass_time());
        imageParam.put("content", fileStoreBean.getContent());
        imageParam.put("head_path", fileStoreBean.getHead_path());
        imageParam.put("bottom_path", fileStoreBean.getBottom_path());
        return HttpClientPoolUtil.jsonPostFromPool(filestoreUrl,  ObjectToJsonUtil.objectToJson(imageParam));

    }
    public static String storeImage(byte[] imgBytes, String passTime,String filestoreUrl) throws Exception {
        Map<String, Object> imageParam = new HashMap<>();
        imageParam.put("file_suffix", "jpg");
        imageParam.put("pass_time", passTime==null? LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")):passTime);
        imageParam.put("content", imgBytes);
        imageParam.put("head_path", "koala,alarm");
        imageParam.put("bottom_path", "");
        return HttpClientPoolUtil.jsonPostFromPool(filestoreUrl, ObjectToJsonUtil.objectToJson(imageParam));
    }

    public static String storeImage(String picUrl, String passTime,String filestoreUrl) throws Exception {
        byte[] imgBytes = HttpClientPoolUtil.httpDownloadImageFromPool(picUrl);
        Map<String, Object> imageParam = new HashMap<>();
        imageParam.put("file_suffix", "jpg");
        imageParam.put("pass_time", passTime);
        imageParam.put("content", imgBytes);
        imageParam.put("head_path", "koala,alarm");
        imageParam.put("bottom_path", "");
        return HttpClientPoolUtil.jsonPostFromPool(filestoreUrl,  ObjectToJsonUtil.objectToJson(imageParam));
    }
}
