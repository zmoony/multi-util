package com.boot.util.wiscom.存储服务.thrift;

import java.nio.ByteBuffer;

import com.boot.util.wiscom.存储服务.bean.FileStoreBean;
import com.boot.util.wiscom.存储服务.bean.FileStoreConfig;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;


import lombok.extern.log4j.Log4j2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 13900
 */
@Log4j2
public class FileStoreThrift {
//    private final  int timeout ;//socket超时和connect超时
    //private static final int FILESTORE_PORT = 16754;
    private     TTransport transport =null;
    private      TProtocol protocol =null;
    private     FileStoreService.Client client=null;
    public  String filestore(FileStoreBean fileStoreBean, FileStoreConfig fileStoreConfig){
        String url=null;
        try{
            if(transport==null){
                transport = new TSocket(
                        fileStoreConfig.getThriftHost(),
                        fileStoreConfig.getThriftPort(),
                        fileStoreConfig.getThriftTimeout());
                transport.open();
                protocol = new  TBinaryProtocol(transport);
                client=new FileStoreService.Client(protocol);
                log.info("thrift socket 重新初始化成功");
            }
            if(transport.isOpen()==false){
                transport.open();
                log.info("thrift socket 重新打开");
            }
            url=client.storeFile(fileStoreBean.getPass_time(), ByteBuffer.wrap(fileStoreBean.getContent()),
                    fileStoreBean.getFile_suffix(), fileStoreBean.getHead_path(), fileStoreBean.getBottom_path());


        }catch(Exception ex){
            //Caused by: java.net.SocketTimeoutException: Read timed out
            log.error("",ex);
           try{
                if(transport!=null){
                    transport.close();
                    //transport=null;  报错 Internal error processing storeFile
                }
               transport = new TSocket(
                       fileStoreConfig.getThriftHost(),
                       fileStoreConfig.getThriftPort(),
                       fileStoreConfig.getThriftTimeout());
                transport.open();
                protocol = new  TBinaryProtocol(transport);
                client=new FileStoreService.Client(protocol);
                log.info("thrift socket 重新初始化成功");
           }catch(Exception ex1){
               log.error("",ex1);
           }

        }
        return url;
    }

}
