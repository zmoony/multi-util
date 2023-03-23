package com.boot.util.ftp;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

/**
 * ftp的链接
 * @author yuez
 */
@Log4j2
public class FtpConnection {
    private String host;
    private int port;
    private String username;
    private String password;
    private String charset;
    /**
     * 获取连接
     * @return FTPClient
     */
    public FTPClient getFtpClient(){
        FTPClient ftpClient=null;
        try {

            ftpClient=new FTPClient();
            // 连接超时
            ftpClient.setConnectTimeout(5000);
            // 读取超时
            ftpClient.setDefaultTimeout(10000);
            ftpClient.connect(host, port);
            //检测连接应答代码是否是一个正确的响应
            int reply =  ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                log.error("FTP 服务器拒绝连接");
            }else{
                //socket连接超时时间
                ftpClient.setSoTimeout(10000);
                ftpClient.login(username, password);
                ////指定上传文件的类型为二进制类型，默认是FTP.ASCII_FILE_TYPE
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.setControlEncoding(charset);
                ftpClient.enterLocalPassiveMode();
            }
        } catch (IOException ex) {
            log.info("Ftp连接异常：{}",ex);
        }
        return ftpClient;
    }


    public FTPClient getFtpClientByPool(){
        try {
            return FtpClientPool.getInstance().ftpClientPool.borrowObject(10000);
        } catch (Exception e) {
            log.info("Ftp连接池获取对象异常：{}",e);
        }
        return null;
    }

    /**
     * 对象使用完一定要归还，要不然用完borrowObject最大使用量就会报错
     * 在finally进行returnObject的归还
     * @param ftpClient
     */
    public void returnFtpClientByPool(FTPClient ftpClient){
        try {
            FtpClientPool.getInstance().ftpClientPool.returnObject(ftpClient);
        } catch (Exception e) {
            log.info("Ftp连接池销毁对象异常：{}",e);
        }
    }
}
