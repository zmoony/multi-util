/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wiscom.psmp2.util;

//import ch.ethz.ssh2.*;

import com.boot.bean.HostBean;
import com.boot.bean.ServiceOperationBean;
import com.boot.bean.WinRMBean;
import com.boot.util.common.JschUtil;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 0表示成功  -1表示远程失败
 *
 * @author 13900
 */
@Deprecated
@Log4j2
public class GanymedSHH2Util {
    public static ServiceOperationBean exec(HostBean hostBean, List<String> commads) {
        return JschUtil.exec(hostBean, commads);
//
//        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
//        Connection conn=null;
//        long start = System.currentTimeMillis();
////        String hostname = "172.18.12.21";
////        String username = "root";
////        String password = "wiscom";
//        try {
//            //创建连接
//            String[] info = hostBean.getUsername().split("[:]");
//
//            if (info.length < 2) {
//                conn = new Connection(hostBean.getHost_ip());
//            } else {
//                conn = new Connection(hostBean.getHost_ip(), Integer.parseInt(info[1]));
//            }
//
//            conn.connect();
//            //添加认证
//            boolean isAuthenticated = conn.authenticateWithPassword(info[0], hostBean.getPassword());
//            if (isAuthenticated == false) throw new IOException("认证失败");
//            //创建会话
//            Session sess = conn.openSession();
//            StringBuilder sb = new StringBuilder();
//            //sb.append("export LC_CTYPE=zh_CN.GBK;");//防止输出乱码
//            sb.append("export LANG=en;");//防止输出乱码
//            for (String commad : commads) {
//                sb.append(commad);
//                sb.append(";");
//
//            }
//            sb.deleteCharAt(sb.length() - 1);
//            log.info("开始执行命令");
//            sess.execCommand(sb.toString());
//            log.info(sb.toString());
//            log.info("结束执行命令");
//            //sess.execCommand("rm -rf /zxx_software/aa.txt");
//            //sess.execCommand("uname -a && date && uptime && who","GBK");
//
//            InputStream stdoutin = new StreamGobbler(sess.getStdout());
//            InputStream stderrin = new StreamGobbler(sess.getStderr());
//
//            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdoutin));
//            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderrin));
//            List<String> stdouts = new ArrayList<>();
//            StringBuilder stdout = new StringBuilder();
//
//
//            while (true) {
//                String line = stdoutReader.readLine();
//                if (line == null)
//                    break;
//                stdouts.add(line);
//                stdout.append(line);
//                stdout.append("\n");
//                log.info(line);
//
//            }
//
//            while (true) {
//                String line = stderrReader.readLine();
//                if (line == null)
//                    break;
//                stdouts.add(line);
//                stdout.append(line);
//                stdout.append("\n");
//                log.info(line);
//            }
//            stdoutin.close();
//            stderrin.close();
//            sess.close();
//           // conn.close();
//
//            serviceOperationBean.setStatusCode(0);
//            serviceOperationBean.setStdOuts(stdouts);
//            serviceOperationBean.setStdOut(stdout.toString());
//
//        } catch (IOException ex) {
//            //Connection timed out: connect
//            //20230525 周星星  the execute request failed
//            //认证失败
//            serviceOperationBean.setStatusCode(-1);
//            serviceOperationBean.setStdOut(ex.getMessage());
//            log.error("",ex);
//        }finally{
//            try{
//                conn.close();
//            }catch(Exception ex){
//
//            }
//
//        }
//        long end = System.currentTimeMillis();
//        log.info("耗时(毫秒)：" + (end - start));
//        return serviceOperationBean;
    }


    public static ServiceOperationBean exec(WinRMBean hostBean, List<String> commads) {
        return JschUtil.exec(hostBean, commads);
//        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
//
//        long start = System.currentTimeMillis();
////        String hostname = "172.18.12.21";
////        String username = "root";
////        String password = "wiscom";
//        try {
//            //创建连接
//            String[] info = hostBean.getUsername().split("[:]");
//            Connection conn;
//            if (info.length < 2) {
//                conn = new Connection(hostBean.getHost_ip());
//            } else {
//                conn = new Connection(hostBean.getHost_ip(), Integer.parseInt(info[1]));
//            }
//
//            conn.connect();
//            //添加认证
//            boolean isAuthenticated = conn.authenticateWithPassword(info[0], hostBean.getPassword());
//            if (isAuthenticated == false) throw new IOException("认证失败");
//            //创建会话
//            Session sess = conn.openSession();
//            StringBuilder sb = new StringBuilder();
//            //sb.append("export LC_CTYPE=zh_CN.GBK;");//防止输出乱码
//            for (String commad : commads) {
//                sb.append(commad);
//                sb.append(";");
//
//            }
//            sb.deleteCharAt(sb.length() - 1);
//            log.info("开始执行命令");
//            sess.execCommand(sb.toString());
//            log.info(sb.toString());
//            log.info("结束执行命令");
//            //sess.execCommand("rm -rf /zxx_software/aa.txt");
//            //sess.execCommand("uname -a && date && uptime && who","GBK");
//
//            InputStream stdoutin = new StreamGobbler(sess.getStdout());
//            InputStream stderrin = new StreamGobbler(sess.getStderr());
//
//            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdoutin));
//            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderrin));
//            List<String> stdouts = new ArrayList<>();
//            StringBuilder stdout = new StringBuilder();
//
//
//            while (true) {
//                String line = stdoutReader.readLine();
//                if (line == null)
//                    break;
//                stdouts.add(line);
//                stdout.append(line);
//                stdout.append("\n");
//                log.info(line);
//
//            }
//
//            while (true) {
//                String line = stderrReader.readLine();
//                if (line == null)
//                    break;
//                stdouts.add(line);
//                stdout.append(line);
//                stdout.append("\n");
//                log.info(line);
//            }
//            stdoutin.close();
//            stderrin.close();
//            sess.close();
//            conn.close();
//
//            serviceOperationBean.setStatusCode(0);
//            serviceOperationBean.setStdOuts(stdouts);
//            serviceOperationBean.setStdOut(stdout.toString());
//
//        } catch (IOException ex) {
//            //Connection timed out: connect
//            //认证失败
//            serviceOperationBean.setStatusCode(-1);
//            serviceOperationBean.setStdOut(ex.getMessage());
//            log.error("",ex);
//        }
//        long end = System.currentTimeMillis();
//        log.info("耗时(毫秒)：" + (end - start));
//        return serviceOperationBean;
    }

    /**
     * && 关联
     *
     * @param hostBean
     * @param commads
     * @return
     */
    public static ServiceOperationBean execbj(HostBean hostBean, List<String> commads, boolean result) {
        return JschUtil.execbj(hostBean, commads, result);
//        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
//
//        long start = System.currentTimeMillis();
//         Connection conn=null;
////        String hostname = "172.18.12.21";
////        String username = "root";
////        String password = "wiscom";
//        try {
//            //创建连接
//            String[] info = hostBean.getUsername().split("[:]");
//
//            if (info.length < 2) {
//                conn = new Connection(hostBean.getHost_ip());
//            } else {
//                conn = new Connection(hostBean.getHost_ip(), Integer.parseInt(info[1]));
//            }
//
//            conn.connect();
//            //添加认证
//            boolean isAuthenticated = conn.authenticateWithPassword(hostBean.getUsername(), hostBean.getPassword());
//            if (isAuthenticated == false) throw new IOException("认证失败");
//            //创建会话
//            Session sess = conn.openSession();
//            StringBuilder sb = new StringBuilder();
//            //sb.append("export LC_CTYPE=zh_CN.GBK;");//防止输出乱码
//            for (int i = 0; i < commads.size(); i++) {
//                sb.append(commads.get(i));
//                if (i == (commads.size() - 1)) {
//                } else {
//                    sb.append(" && ");
//                }
//            }
//            log.info("开始执行命令");
//            sess.execCommand(sb.toString());
//            log.info(sb.toString());
//            log.info("结束执行命令");
//            //sess.execCommand("rm -rf /zxx_software/aa.txt");
//            //sess.execCommand("uname -a && date && uptime && who","GBK");
//            if (result == false) {
//                serviceOperationBean.setStatusCode(0);
//                sess.close();
//                conn.close();
//            } else {
//                InputStream stdoutin = new StreamGobbler(sess.getStdout());
//                InputStream stderrin = new StreamGobbler(sess.getStderr());
//
//
//                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdoutin));
//                BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderrin));
//                List<String> stdouts = new ArrayList<>();
//                StringBuilder stdout = new StringBuilder();
//
//
//                while (true) {
//
//                    String line = stdoutReader.readLine();
//                    if (line == null)
//                        break;
//                    stdouts.add(line);
//                    stdout.append(line);
//                    stdout.append("\n");
//                    log.info(line);
//
//                }
//
//                while (true) {
//                    String line = stderrReader.readLine();
//                    if (line == null)
//                        break;
//                    stdouts.add(line);
//                    stdout.append(line);
//                    stdout.append("\n");
//                    log.info(line);
//                }
//                stdoutin.close();
//                stderrin.close();
//                sess.close();
//                //conn.close();
//
//                serviceOperationBean.setStatusCode(0);
//                serviceOperationBean.setStdOuts(stdouts);
//                serviceOperationBean.setStdOut(stdout.toString());
//            }
//
//        } catch (Exception ex) {
//            //Connection timed out: connect
//            //认证失败
//            serviceOperationBean.setStatusCode(-1);
//            serviceOperationBean.setStdOut(ex.getMessage());
//            log.error("", ex);
//        }finally {
//            try{
//                conn.close();
//            }catch(Exception e){
//                log.error(e.getMessage());
//            }
//        }
////        log.info("命令返回结果：" +ObjectToJsonUtil.objectToJson(serviceOperationBean));
//        long end = System.currentTimeMillis();
//        log.info("耗时(毫秒)：" + (end - start));
//        return serviceOperationBean;
    }

    /**
     * params
     * operation  支持 config-read
     *
     * @param hostBean
     * @param params
     * @return
     */
    public static ServiceOperationBean fileOperation(HostBean hostBean, Map<String, Object> params) {
return JschUtil.fileOperation(hostBean,params);
//        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
//
//        long start = System.currentTimeMillis();
////        String hostname = "172.18.12.21";
////        String username = "root";
////        String password = "wiscom";
//        try {
//            //创建连接
//            Connection conn = null;
//            String[] info = hostBean.getUsername().split("[:]");
//
//            if (info.length < 2) {
//                conn = new Connection(hostBean.getHost_ip());
//            } else {
//                conn = new Connection(hostBean.getHost_ip(), Integer.parseInt(info[1]));
//            }
//            conn.connect();
//            //添加认证
//            boolean isAuthenticated = conn.authenticateWithPassword(hostBean.getUsername(), hostBean.getPassword());
//            if (isAuthenticated == false) throw new IOException("认证失败");
//            //创建会话
//            SCPClient scpClient = conn.createSCPClient();
//            List<String> stdouts = new ArrayList<>();
//            StringBuilder stdout = new StringBuilder();
//            String fullname = params.get("fullname") + "";
//            String operation = params.get("operation") + "";
//            String encode="UTF-8";
//            //params.containsKey("encode")
//            if(params.get("encode")!=null){
//                encode=params.get("encode").toString();
//            }
//
//            switch (operation) {
//                case "config-read":
//
//                    SCPInputStream sCPInputStream = scpClient.get(fullname);//读取远程文件  继承BufferedInputStream
//                    BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(sCPInputStream,encode));
//
//                    while (true) {
//
//                        String line = stdoutReader.readLine();
//                        if (line == null)
//                            break;
//                        stdouts.add(line);
//                        stdout.append(line);
//                        stdout.append("\n");
//                        log.info(line);
//
//                    }
//                    sCPInputStream.close();
//                    break;
//                case "config-write":
//                    int weizhi = fullname.lastIndexOf("/");
//                    String remoteFile = fullname.substring(weizhi + 1);
//                    String remoteTargetDirectory = fullname.substring(0, weizhi + 1);
//                    List<String> contents = (List<String>) params.get("content");
//                    for (int i = 0; i < contents.size(); i++) {
//                        if (i == (contents.size() - 1)) {
//                            stdout.append(contents.get(i));
//                        } else {
//                            stdout.append(contents.get(i)).append(System.getProperty("line.separator"));
//                        }
//                    }
//                    byte[] lastcontent = stdout.toString().getBytes("utf-8");
//                    SCPOutputStream sCPOutputStream = scpClient.put(remoteFile, lastcontent.length, remoteTargetDirectory, "0644");
//                    sCPOutputStream.write(lastcontent);
//                    sCPOutputStream.flush();
//                    sCPOutputStream.close();
//
//                    break;
//
//            }
//            conn.close();
//
//            serviceOperationBean.setStatusCode(0);
//            serviceOperationBean.setStdOuts(stdouts);
//            serviceOperationBean.setStdOut(stdout.toString());
//
//
//        } catch (IOException ex) {
//            //Connection timed out: connect
//            //认证失败
//            serviceOperationBean.setStatusCode(-1);
//            serviceOperationBean.setStdOut(ex.getMessage());
//            log.error("",ex);
//        }
//        long end = System.currentTimeMillis();
//        log.info("耗时(毫秒)：" + (end - start));
//        return serviceOperationBean;
    }

    /***
     　　* @description: linux下载文件
     　　* @param [remoteFilePath, localFilePath, hostBean]
     　　* @return boolean
     　　* @throws
     　　* @author yuez
     　　* @date 2020/12/9 9:39
     　　*/
    public static boolean sftpDownload(String remoteFilePath, String localFilePath, HostBean hostBean) {
        return JschUtil.sftpDownload(remoteFilePath, localFilePath, hostBean);
//        boolean bool;
//        Connection connection = null;
//        SCPInputStream scpInputStream = null;
//        BufferedReader stdoutReader = null;
//        FileOutputStream fileOutputStream = null;
//        try {
//            //创建连接
//            String[] info = hostBean.getUsername().split("[:]");
//
//            if (info.length < 2) {
//                connection = new Connection(hostBean.getHost_ip());
//            } else {
//                connection = new Connection(hostBean.getHost_ip(), Integer.parseInt(info[1]));
//            }
//            connection.connect();
//            //添加认证
//            boolean isAuthenticated = connection.authenticateWithPassword(hostBean.getUsername(), hostBean.getPassword());
//            if (isAuthenticated == false) throw new IOException("认证失败");
//            //获取远程文件
//            SCPClient scpClient = connection.createSCPClient();
//            scpInputStream = scpClient.get(remoteFilePath);
//            stdoutReader = new BufferedReader(new InputStreamReader(scpInputStream));
//            //将流写到文件
//            File file = new File(localFilePath);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            fileOutputStream = new FileOutputStream(localFilePath);
//            String line;
//            while ((line = stdoutReader.readLine()) != null) {
//                fileOutputStream.write(line.getBytes("GBK"));
//            }
//            fileOutputStream.flush();
//            bool = true;
//        } catch (IOException ioe) {
//            log.error("【下载日志linux】错误：{}", ioe);
//            bool = false;
//        } finally {
//            try {
//                if (fileOutputStream != null) fileOutputStream.close();
//                if (scpInputStream != null) scpInputStream.close();
////                if(stdoutReader != null) stdoutReader.close();
//                if (connection != null) connection.close();
//            } catch (IOException e) {
//                log.error("【下载日志linux】关闭流错误：{}", e);
//            }
//        }
//        return bool;
    }

    /**
     * 向远程服务器上传文件
     *
     * @param hostBean       远程服务器参数
     * @param inputStream    输入流，注意：代码中会使用inputStream.available()方法，在请求的inputStream中可能会存在available()为0的情况，请注意
     * @param targetFileName 目标文件名
     * @param targetPath     目标路径
     * @return 返回值
     * @author zaiji
     */
    public static ServiceOperationBean SCPPut(InputStream inputStream, WinRMBean hostBean, String targetPath, String targetFileName) {
        return JschUtil.SCPPut(inputStream, hostBean, targetPath, targetFileName);
//        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
//        try {
//            //创建连接
//            Connection conn = null;
//            String[] info = hostBean.getUsername().split("[:]");
//
//            if (info.length < 2) {
//                conn = new Connection(hostBean.getHost_ip());
//            } else {
//                conn = new Connection(hostBean.getHost_ip(), Integer.parseInt(info[1]));
//            }
//            conn.connect();
//            //添加认证
//            boolean isAuthenticated = conn.authenticateWithPassword(hostBean.getUsername(), hostBean.getPassword());
//            if (isAuthenticated == false) {
//                throw new IOException("认证失败");
//            }
//            //创建会话
//            SCPClient scpClient = conn.createSCPClient();
//
//            SCPOutputStream sCPOutputStream = scpClient.put(targetFileName, inputStream.available(), targetPath, "0644");
//
//            int len;
//            byte[] buffer = new byte[1024 * 5];
//
//            while ((len = inputStream.read(buffer)) != -1) {
//                sCPOutputStream.write(buffer, 0, len);
//            }
//            sCPOutputStream.flush();
//            sCPOutputStream.close();
//            conn.close();
//
//            serviceOperationBean.setStatusCode(0);
//            serviceOperationBean.setStdOut("拷贝文件到远程成功");
//            return serviceOperationBean;
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            serviceOperationBean.setStatusCode(-1);
//            serviceOperationBean.setStdOut("拷贝文件到远程异常：" + ex.getMessage());
//            return serviceOperationBean;
//        }
    }

    /**
     * 从远程服务器下载文件
     *
     * @param hostBean     远程服务器参数
     * @param outputStream 输出流
     * @param targetPath   目标文件
     * @return 返回值
     * @author zaiji
     */
    public static ServiceOperationBean SCPGet(OutputStream outputStream, WinRMBean hostBean, String targetPath) {
        log.info("目标文件：{}"+targetPath);
        return  JschUtil.SCPGet(outputStream, hostBean, targetPath);
//        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
//        try {
//            //创建连接
//            Connection conn = null;
//            String[] info = hostBean.getUsername().split("[:]");
//
//            if (info.length < 2) {
//                conn = new Connection(hostBean.getHost_ip());
//            } else {
//                conn = new Connection(hostBean.getHost_ip(), Integer.parseInt(info[1]));
//            }
//            conn.connect();
//            //添加认证
//            boolean isAuthenticated = conn.authenticateWithPassword(hostBean.getUsername(), hostBean.getPassword());
//            if (isAuthenticated == false) {
//                throw new IOException("认证失败");
//            }
//            //创建会话
//            SCPClient scpClient = conn.createSCPClient();
//
//            SCPInputStream scpInputStream = scpClient.get(targetPath);
//
//            int len;
//            byte[] buffer = new byte[1024 * 5];
//
//            while ((len = scpInputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, len);
//            }
//            outputStream.flush();
//            scpInputStream.close();
//            conn.close();
//
//            serviceOperationBean.setStatusCode(0);
//            serviceOperationBean.setStdOut("拷贝文件到远程成功");
//            return serviceOperationBean;
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            serviceOperationBean.setStatusCode(-1);
//            serviceOperationBean.setStdOut("拷贝文件到远程异常：" + ex.getMessage());
//            return serviceOperationBean;
//        }
    }



}
