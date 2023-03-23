package com.boot.util.ftp;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

/**
 * ftp对象工厂
 * 采用第三方-commons-pool2
 * addObject方法：往池中添加一个对象。池子里的所有对象都是通过这个方法进来的。
 * borrowObject方法：从池中借走到一个对象。借走不等于删除。对象一直都属于池子，只是状态的变化。
 * returnObject方法：把对象归还给对象池。归还不等于添加。对象一直都属于池子，只是状态的变化。
 * invalidateObject：销毁一个对象。这个方法才会将对象从池子中删除，当然这其中最重要的就是释放对象本身持有的各种资源。
 * getNumIdle：返回对象池中有多少对象是空闲的，也就是能够被借走的对象的数量。
 * getNumActive：返回对象池中有对象对象是活跃的，也就是已经被借走的，在使用中的对象的数量。
 * clear：清理对象池。注意是清理不是清空，改方法要求的是，清理所有空闲对象，释放相关资源。
 * close：关闭对象池。这个方法可以达到清空的效果，清理所有对象以及相关资源。
 *
 * tip:
 * returnObject和borrowObject应该成对的互斥操作。
 * 所谓的borrowObject不是从池子里真的移除。
 * 所谓的returnObject也不是真的往池子里添加。
 * 对象一直都在大池子里，是用状态来区分是被借走了、还是已经归还了（空闲了）
 * @author yuez
 * @since 2022/6/14
 */
@Log4j2
@NoArgsConstructor
public class FtpClientFactory extends BasePooledObjectFactory<FTPClient> {
    private String host;
    private int port;
    private String username;
    private String password;
    private String charset;

    public void init(String host, int port, String username, String password,String charset) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.charset = charset;
    }

    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        // 连接超时
        ftpClient.setConnectTimeout(5000);
        // 读取超时
        ftpClient.setDefaultTimeout(10000);
        try {
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
            }
        } catch (Exception ex) {
            log.info("Ftp连接异常：{}", ex);
        }

        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) throws Exception {
        if (ftpPooled != null) {
            return;
        }
        FTPClient ftpClient = ftpPooled.getObject();
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
            }
        }catch (Exception e){
            log.error("ftp退出失败：{}",e);
        }finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("关闭ftp失败：{}",e);
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        try {
            FTPClient ftpClient = p.getObject();
            return ftpClient.sendNoOp();
        } catch (Exception e) {
            log.error("验证ftpclient失败：{}",e);
        }
        return false;
    }
}
