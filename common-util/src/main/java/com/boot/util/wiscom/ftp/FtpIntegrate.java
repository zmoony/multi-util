package com.boot.util.wiscom.ftp;

import com.boot.util.wiscom.GlobalObject;
import com.boot.util.wiscom.HttpClientPoolUtil;
import com.boot.util.wiscom.ImageUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@Log4j2
/**
 * ftp集成
 * @author hwang
 * */
public class FtpIntegrate {

    /**
     * 下载图片文件,存入ftp
     */
    public FtpStatusBean sendImageToFtp(String strDirPath, String strFileName, String strImageUrl) {
        boolean sendFlag =false;
        FtpStatusBean statusBean=new FtpStatusBean();
        FtpConnection ftpConnection = new FtpConnection();
        FTPClient ftpClient = ftpConnection.getFTPClient();
        if (ftpClient != null) {
            try {
                //切换到指定目录
                boolean flag = ftpClient.changeWorkingDirectory(strDirPath);
                //判断目录是否存在
                if (!flag) {
                    if (!makeDirectory(ftpClient, strDirPath)) {
                        statusBean.setFlag(false);
                        statusBean.setErrormsg("生成目录" + strDirPath + "失败");
                        return statusBean;
                    }
                    ftpClient.changeWorkingDirectory(strDirPath);
                }
                //下载文件
                byte[] content = HttpClientPoolUtil.getPicByte(strImageUrl,1);
                if (null == content) {
                    statusBean.setFlag(false);
                    statusBean.setErrormsg("下载文件" + strImageUrl + "失败");
                    return statusBean;
                }
                Long illegalNotePicSize = Math.round(Double.parseDouble(GlobalObject.properties_business_my.getProperty("illegalnote.pic.size")) * 1024);
                if (content.length > illegalNotePicSize) {
                    log.info("开始压缩图片");
                    long start = System.currentTimeMillis();
                    content = ImageUtil.compressPicture(content, illegalNotePicSize);
                    log.info("压缩图片耗时{}ms",System.currentTimeMillis() - start);
                }
                //指定上传的文件名，并返回该文件的输出流
                OutputStream outputstream = ftpClient.storeFileStream(strFileName);
                //上传文件
                outputstream.write(content);
                outputstream.close();
                sendFlag = true;
                statusBean.setErrormsg("Image存入ftp成功");
                log.info("Image存入ftp成功" + strFileName);
            } catch (Exception e) {
                log.error("生成文件存在异常:" + e);
                sendFlag=false;
                statusBean.setErrormsg("文件上传ftp存在异常"+e);
            } finally {
                disconnect(ftpClient);
            }
            statusBean.setFlag(sendFlag);
        }
        else{
            statusBean.setFlag(false);
            statusBean.setErrormsg("ftp连接失败");
        }
        return statusBean;
    }

    public boolean makeDirectory(FTPClient ftpClient,String dir) {
        boolean flag = false;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                log.info("创建文件夹" + dir + " 成功！");

            } else {
                log.info("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            log.error("" + e.getMessage());
        }
        return flag;
    }

    /**
     * 删除接收目录中的请求文件
     * @param fileName
     * @return
     */
    public boolean deleteFile(String DirPath,String fileName){
        boolean flag=false;
        FtpConnection ftpConnection=new FtpConnection();
        FTPClient ftpClient=ftpConnection.getFTPClient();
        if(ftpClient !=null){
            String changePath=DirPath;
            try {
                //切换到指定目录
                ftpClient.changeWorkingDirectory(changePath);
                //指定上传的文件名，并返回该文件的输出流
                flag=ftpClient.deleteFile(fileName);
            } catch (IOException ex) {
                log.info("删除文件发生异常："+ex.getMessage());
            }finally{
                disconnect(ftpClient);
            }
        }

        return flag;
    }
    /**
     * 列出目录中的文件名称
     */
    public String[] listFiles(String strDir) {
        FtpConnection ftpConnection = new FtpConnection();
        FTPClient client = ftpConnection.getFTPClient();
        if (client != null) {
            try {
                client.changeWorkingDirectory(strDir);
                String[] names = client.listNames();
                return names;
            } catch (IOException ex) {
                log.info("列出文件名发生异常："+ex.getMessage());
            } finally{
                disconnect(client);
            }
        }
        return null;
    }

    public InputStream getFileStream(String filename) {
        FtpConnection ftpConnection = new FtpConnection();
        FTPClient client = ftpConnection.getFTPClient();
        if (client != null) {
            try {
                return client.retrieveFileStream(filename);
            } catch (IOException ex) {
                log.info("读取文件发生异常："+ex.getMessage());
            } finally {
                disconnect(client);
            }
        }
        return null;
    }

    private void disconnect(FTPClient ftpClient) {
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
