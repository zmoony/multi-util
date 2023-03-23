package com.boot.util.ftp;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

/**
 * ftp的操作集合
 * @author yuez
 */
@Log4j2
public class FileFtpDao {
    public FTPFile[] getFtpFiles(String dirPath) {
        FTPFile[] files = null;
        FtpConnection ftpConnection = new FtpConnection();
        FTPClient ftpClient = ftpConnection.getFtpClient();
        if (ftpClient != null) {
            try {
                //切换到指定目录
                boolean flag = ftpClient.changeWorkingDirectory(dirPath);
                //判断目录是否存在
                if (flag) {
                    files = ftpClient.listFiles();
                } else {
                    log.error("发送目录不存在：" + ftpClient.getReplyString());
                }
            } catch (Exception e) {
                log.error("获取文件存在异常:" + e);
            } finally {
                disconnect(ftpClient);
            }
        }
        return files;
    }


    public void disconnect(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ioe) {
                // do nothing
            }
        }
    }

}
