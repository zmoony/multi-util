package com.boot.util.common;

import com.boot.bean.HostBean;
import com.boot.bean.ServiceOperationBean;
import com.boot.bean.WinRMBean;
import com.jcraft.jsch.*;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class JschUtil {

    public static ServiceOperationBean exec(HostBean hostBean, List<String> commands) {
        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        long start = System.currentTimeMillis();

        try {
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            StringBuilder command = new StringBuilder();
            for (String cmd : commands) {
                command.append(cmd).append(";");
            }
            channel.setCommand(command.toString());

            InputStream stdout = channel.getInputStream();
            InputStream stderr = channel.getErrStream();
            channel.connect();

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
            List<String> stdouts = new ArrayList<>();
            StringBuilder stdoutContent = new StringBuilder();

            String line;
            while ((line = stdoutReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

            while ((line = stderrReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

//            serviceOperationBean.set(0);
            serviceOperationBean.setStdOuts(stdouts);
            serviceOperationBean.setStdOut(stdoutContent.toString());

        } catch (JSchException | IOException ex) {
//            serviceOperationBean.setStatusCode(-1);
            serviceOperationBean.setStdOut(ex.getMessage());
            log.error("", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        long end = System.currentTimeMillis();
        log.info("耗时(毫秒)：" + (end - start));
        return serviceOperationBean;
    }

    public static ServiceOperationBean exec(HostBean hostBean, List<String> commands,boolean async) {
        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        long start = System.currentTimeMillis();

        try {
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            StringBuilder command = new StringBuilder();
            if(async){
                for (String cmd : commands) {
                    command.append(cmd).append(";");
                }
            }else{
                for (String cmd : commands) {
                    command.append(cmd).append("&&");
                }
                command.deleteCharAt(command.length()-1);
                command.deleteCharAt(command.length()-1);
                command.append(";");
            }

            channel.setCommand(command.toString());

            InputStream stdout = channel.getInputStream();
            InputStream stderr = channel.getErrStream();
            channel.connect();

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
            List<String> stdouts = new ArrayList<>();
            StringBuilder stdoutContent = new StringBuilder();

            String line;
            while ((line = stdoutReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

            while ((line = stderrReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

//            serviceOperationBean.set(0);
            serviceOperationBean.setStdOuts(stdouts);
            serviceOperationBean.setStdOut(stdoutContent.toString());

        } catch (JSchException | IOException ex) {
//            serviceOperationBean.setStatusCode(-1);
            serviceOperationBean.setStdOut(ex.getMessage());
            log.error("", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        long end = System.currentTimeMillis();
        log.info("耗时(毫秒)：" + (end - start));
        return serviceOperationBean;
    }

    public static ServiceOperationBean exec(WinRMBean hostBean, List<String> commands) {
        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        long start = System.currentTimeMillis();

        try {
            session = jsch.getSession(hostBean.getUsername(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            StringBuilder command = new StringBuilder();
            for (String cmd : commands) {
                command.append(cmd).append(";");
            }
            channel.setCommand(command.toString());

            InputStream stdout = channel.getInputStream();
            InputStream stderr = channel.getErrStream();
            channel.connect();

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
            List<String> stdouts = new ArrayList<>();
            StringBuilder stdoutContent = new StringBuilder();

            String line;
            while ((line = stdoutReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

            while ((line = stderrReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

            serviceOperationBean.setStatusCode(0);
            serviceOperationBean.setStdOuts(stdouts);
            serviceOperationBean.setStdOut(stdoutContent.toString());

        } catch (JSchException | IOException ex) {
            serviceOperationBean.setStatusCode(-1);
            serviceOperationBean.setStdOut(ex.getMessage());
            log.error("", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        long end = System.currentTimeMillis();
        log.info("耗时(毫秒)：" + (end - start));
        return serviceOperationBean;
    }

    public static ServiceOperationBean execbj(HostBean hostBean, List<String> commands, boolean result) {
        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        long start = System.currentTimeMillis();

        try {
            session = jsch.getSession(hostBean.getUsername(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            StringBuilder command = new StringBuilder();
            for (int i = 0; i < commands.size(); i++) {
                command.append(commands.get(i));
                if (i != (commands.size() - 1)) {
                    command.append(" && ");
                }
            }
            channel.setCommand(command.toString());

            if (!result) {
                channel.connect();
                serviceOperationBean.setStatusCode(0);
                return serviceOperationBean;
            }

            InputStream stdout = channel.getInputStream();
            InputStream stderr = channel.getErrStream();
            channel.connect();

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
            List<String> stdouts = new ArrayList<>();
            StringBuilder stdoutContent = new StringBuilder();

            String line;
            while ((line = stdoutReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

            while ((line = stderrReader.readLine()) != null) {
                stdouts.add(line);
                stdoutContent.append(line).append("\n");
                log.info(line);
            }

            serviceOperationBean.setStatusCode(0);
            serviceOperationBean.setStdOuts(stdouts);
            serviceOperationBean.setStdOut(stdoutContent.toString());

        } catch (JSchException | IOException ex) {
            serviceOperationBean.setStatusCode(-1);
            serviceOperationBean.setStdOut(ex.getMessage());
            log.error("", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        long end = System.currentTimeMillis();
        log.info("耗时(毫秒)：" + (end - start));
        return serviceOperationBean;
    }

    public static ServiceOperationBean fileOperation(HostBean hostBean, Map<String, Object> params) {
        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;
        long start = System.currentTimeMillis();

        try {
            session = jsch.getSession(hostBean.getUsername(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            String fullname = params.get("fullname").toString();
            String operation = params.get("operation").toString();
            String encode = "UTF-8";
            List<String> stdouts = new ArrayList<>();
            StringBuilder stdout = new StringBuilder();

            switch (operation) {
                case "config-read":
                    InputStream inputStream = channel.get(fullname);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encode));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stdouts.add(line);
                        stdout.append(line).append("\n");
                        log.info(line);
                    }
                    inputStream.close();
                    break;
                case "config-write":
                    List<String> contents = (List<String>) params.get("content");
                    for (String content : contents) {
                        stdout.append(content).append(System.lineSeparator());
                    }
                    byte[] contentBytes = stdout.toString().getBytes(encode);
                    OutputStream outputStream = channel.put(fullname);
                    outputStream.write(contentBytes);
                    outputStream.close();
                    break;
            }

            serviceOperationBean.setStatusCode(0);
            serviceOperationBean.setStdOuts(stdouts);
            serviceOperationBean.setStdOut(stdout.toString());

        } catch (JSchException | SftpException | IOException ex) {
//            serviceOperationBean.setStatusCode(-1);
            serviceOperationBean.setStdOut(ex.getMessage());
            log.error("", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        long end = System.currentTimeMillis();
        log.info("耗时(毫秒)：" + (end - start));
        return serviceOperationBean;
    }

    public static boolean sftpDownload(String remoteFilePath, String localFilePath, HostBean hostBean) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;
        boolean success = false;

        try {
            session = jsch.getSession(hostBean.getUsername(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            channel.get(remoteFilePath, localFilePath);
            success = true;

        } catch (JSchException | SftpException ex) {
            log.error("【下载日志linux】错误：{}", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        return success;
    }

    public static ServiceOperationBean SCPPut(InputStream inputStream, WinRMBean hostBean, String targetPath, String targetFileName) {
        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;

        try {
            session = jsch.getSession(hostBean.getUsername(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            channel.put(inputStream, targetPath + "/" + targetFileName);

//            serviceOperationBean.setStatusCode(0);
            serviceOperationBean.setStdOut("拷贝文件到远程成功");

        } catch (JSchException | SftpException ex) {
//            serviceOperationBean.setStatusCode(-1);
            serviceOperationBean.setStdOut("拷贝文件到远程异常：" + ex.getMessage());
            log.error("", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        return serviceOperationBean;
    }

    public static ServiceOperationBean SCPGet(OutputStream outputStream, WinRMBean hostBean, String targetPath) {
        ServiceOperationBean serviceOperationBean = new ServiceOperationBean();
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;

        try {
            session = jsch.getSession(hostBean.getUsername(), hostBean.getHost_ip(), 22);
            session.setPassword(hostBean.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            InputStream inputStream = channel.get(targetPath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            inputStream.close();

//            serviceOperationBean.setStatusCode(0);
            serviceOperationBean.setStdOut("拷贝文件到远程成功");

        } catch (JSchException | SftpException | IOException ex) {
//            serviceOperationBean.setStatusCode(-1);
            serviceOperationBean.setStdOut("拷贝文件到远程异常：" + ex.getMessage());
            log.error("", ex);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        return serviceOperationBean;
    }
}
