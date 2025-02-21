package com.boot.util.wiscom.ftp;

import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

@Log4j2
/**
 * ftp连接初始化
 * @author zf
 */
public class FtpConnection {
    /**
     * 获取连接
     * @return
     */
    public FTPClient getFTPClient() {
        String host = GlobalObject.properties_business_my.getProperty("wiscom.ftp.server.host").trim();
        int port = Integer.parseInt(GlobalObject.properties_business_my.getProperty("wiscom.ftp.server.port").trim());
        String username = GlobalObject.properties_business_my.getProperty("wiscom.ftp.server.username").trim();
        String password = GlobalObject.properties_business_my.getProperty("wiscom.ftp.server.password").trim();
        int timeout = Integer.parseInt(GlobalObject.properties_business_my.getProperty("wiscom.ftp.timeout"));
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(host, port);
            log.debug("ftp[" + host + ":" + port + "]连接...");
            //超时设置
            ftpClient.setConnectTimeout(timeout*1000);
            ftpClient.setDefaultTimeout(timeout*1000);
            ftpClient.setSoTimeout(timeout*1000);
            //检测连接应答代码是否是一个正确的响应
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                log.error("FTP 服务器拒绝连接");
            } else {
                //指定上传文件的类型为二进制类型，默认是FTP.ASCII_FILE_TYPE
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.setControlEncoding("utf-8");

                if (ftpClient.login(username, password)) {
                    log.debug("ftp login[" + username + ":" + password + "]success.");
                } else {
                    log.error("ftp login[" + username + ":" + password + "]failed!");
                    return null;
                }
            }
        } catch (IOException ex) {
            log.info("Ftp连接异常：" + ex.getMessage());
        }
        return ftpClient;
    }
}
