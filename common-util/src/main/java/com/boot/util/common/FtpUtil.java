package com.boot.util.common;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

/**
 * @author yuez
 * @title: FtpUtil
 * @projectName PSMP3
 * @description: ftp工具类
 * @date 2020/10/19 13:56
 */
@Log4j2
public class FtpUtil {
    private static FtpUtil INSTANCE = new FtpUtil();
    private FtpUtil(){}
    public static FtpUtil getINSTANCE (){return INSTANCE;};
    //从本地文件获取各种属性
    private FTPClient ftpClient;
    private String ftpEncode = "GBK";
    private static String ftpIP="172.17.112.115";
    private static Integer ftpPort=9216;
    private static String ftpUserName="wiscom";
    private static String ftpPassword="wiscom";

    //测试连接
    public synchronized boolean connectServer() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding(ftpEncode);//解决上传文件时文件名乱码
        int reply = 0 ;
        try {
            // 连接至服务器
            ftpClient.connect(ftpIP,ftpPort);
            // 登录服务器
            ftpClient.login(ftpUserName,ftpPassword);
            //登陆成功，返回码是230
            reply = ftpClient.getReplyCode();
            // 判断返回码是否合法
            if(!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    //判断ftp服务器文件夹是否存在
    public boolean existDir(String path)  {
        boolean flag = false;
        try {
           ftpClient.changeWorkingDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
    public boolean existDirOrCreate(String path)  {
        boolean flag = false;
        try {
            ftpClient.changeWorkingDirectory(path);
        } catch (IOException e) {
            try {
                ftpClient.makeDirectory(path);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return flag;
    }
    //判断ftp服务文件夹是否为空文件夹
    public boolean existFile(String path)  {
        boolean flag = false;
        FTPFile[] ftpFileArr;
        try {
            ftpFileArr = ftpClient.listFiles(path);
            if (ftpFileArr.length > 0) {
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
    //删除ftp文件
    public synchronized boolean deleteFile(String pathname, String filename){
        boolean flag = false;
        try {
            System.out.println("开始删除文件");
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
            System.out.println("删除文件成功");
        } catch (Exception e) {
            System.out.println("删除文件失败");
            e.printStackTrace();
        } finally {
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }
    //从FTP server下载到本地文件夹
    public synchronized boolean download(String path , String localFile){
        boolean flag = false;
        FTPFile[] fs=null;
        try {
            fs = ftpClient.listFiles(path);
            if(fs.length<0) {
                return flag;
            }
            //1、遍历FTP路径下所有文件
            for(FTPFile file:fs){
                //2、保存到本地
                OutputStream os = new FileOutputStream(localFile);
                //retrieveFile(FTP服务端的源路径),这个路径要和listFiles的路径一致
                ftpClient.retrieveFile(path + file.getName(), os);
                //3、删除FTP中的上面保存的文件
                //循环外关闭，读一个关闭一次挺慢的
                os.flush();
                os.close();
            }
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
    public boolean checkSubfolder(String path, String subfolderName) {
        try {
            //切换到FTP根目录
            ftpClient.changeWorkingDirectory(path);
            //查看根目录下是否存在该文件夹
            InputStream is = ftpClient.retrieveFileStream(new String(subfolderName.getBytes(ftpEncode)));
            if (is == null || ftpClient.getReplyCode() == FTPReply.FILE_UNAVAILABLE) {
                //若不存在该文件夹，则创建文件夹
                return createSubfolder(path,subfolderName);
            }
            if (is != null) {
                is.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean createSubfolder(String path,String subfolderName){
        try {
            ftpClient.changeWorkingDirectory(path);
            ftpClient.makeDirectory(subfolderName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    //上传文件
    public synchronized boolean upload(InputStream inputStream , String fileName , String path) {
        try {
            //切换工作路径，设置上传的路径
            ftpClient.changeWorkingDirectory(path);
            //设置1M缓冲
            ftpClient.setBufferSize(1024);
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 设置以二进制方式传输
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            /*
             * 第一个参数：服务器端文档名
             * 第二个参数：上传文档的inputStream
             * 在前面设置好路径，缓冲，编码，文件类型后，开始上传
             */
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeClient();
        }
    }

    /**
     * 断开与远程服务器的连接
     */
    public void closeClient(){
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
