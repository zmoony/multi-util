package com.boot.util.wiscom.存储服务.http;

import com.boot.util.wiscom.HttpClientPoolUtil;
import com.boot.util.wiscom.存储服务.bean.FileStoreBean;
import com.boot.util.wiscom.存储服务.bean.FileStoreConfig;

/**
 * Main
 *
 * @author yuez
 * @since 2025/2/18
 */
public class Main {
    public static void main(String[] args) {
        String picUrl = "http://127.0.0.1:9090/upload/1679445521171.jpg";
        byte[] imgBytes = HttpClientPoolUtil.httpDownloadImageFromPool(picUrl);
        FileStoreBean fileStoreBean = new FileStoreBean();
        fileStoreBean.setPass_time("20250218114400");
        fileStoreBean.setContent(imgBytes);
        fileStoreBean.setFile_suffix("jpg");
        fileStoreBean.setHead_path("face,alarm");
        fileStoreBean.setBottom_path("320104155692332");
        try {
            String storeImage = PicUtil.filestore(fileStoreBean, new FileStoreConfig("http://32.33.207.123:9223/send/wiscom/json", "", 0,0));
            System.out.println(storeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
