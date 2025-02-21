package com.boot.util.wiscom.存储服务.thrift;

import com.boot.util.wiscom.HttpClientPoolUtil;
import com.boot.util.wiscom.存储服务.bean.FileStoreBean;
import com.boot.util.wiscom.存储服务.bean.FileStoreConfig;
import com.boot.util.wiscom.存储服务.http.PicUtil;

/**
 * Main
 *
 * @author yuez
 * @since 2025/2/18
 */
public class Main {
    public static void main(String[] args) {
        FileStoreThrift fileStoreThrift=new FileStoreThrift();
        String picUrl = "http://127.0.0.1:9090/upload/1679445521171.jpg";
        byte[] imgBytes = HttpClientPoolUtil.httpDownloadImageFromPool(picUrl);
        FileStoreBean fileStoreBean = new FileStoreBean();
        fileStoreBean.setPass_time("20250218114400");
        fileStoreBean.setContent(imgBytes);
        fileStoreBean.setFile_suffix("jpg");
        fileStoreBean.setHead_path("face,alarm");
        fileStoreBean.setBottom_path("320104155692332");
        try {
            String storeImage = fileStoreThrift.filestore(fileStoreBean, new FileStoreConfig("", "32.33.207.123", 16754,3000));
            System.out.println(storeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
