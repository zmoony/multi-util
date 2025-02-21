package com.boot.util.common;


import com.boot.bean.HostBean;
import com.boot.bean.ServiceOperationBean;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Log4j2
public class LinuxSSHUtil {
    //private final  SSHClient client = new SSHClient();

    /**
     *
     * @param hostBean 服务器信息
     * @param commads 命令
     * @param async 命令之间是否异步
     * @return
     */
    public  static ServiceOperationBean exec(HostBean hostBean, List<String> commads, boolean async){
        if(StringUtils.hasLength(hostBean.getPort())){
            hostBean.setPort(22+"");
        }
        return execByJSCH2(hostBean,commads,async);
    }
    public  static ServiceOperationBean upload(HostBean hostBean,String local_file,String remote_file){
        return uploadByJSCH(hostBean,local_file,remote_file);
    }
    public  static ServiceOperationBean upload(HostBean hostBean,InputStream in,String remote_file){
        return uploadByJSCH(hostBean,in,remote_file);
    }
    public  static ServiceOperationBean download(HostBean hostBean, String remote_file,String local_file){
        return downloadByJSCH(hostBean,remote_file,local_file);
    }
    public  static ServiceOperationBean read(HostBean hostBean,String remote_file,String encode){
        return readByJSCH(hostBean,remote_file,encode);
    }
    public  static ServiceOperationBean write(HostBean hostBean,String remote_file,byte[] content){
        return writeByJSCH(hostBean,remote_file,content);
    }


    /**
     * 采用分号，命令是依次执行
     * @param hostBean
     * @param commads
     * @param async
     * @return
     */
    public static ServiceOperationBean execByJSCH(HostBean hostBean, List<String> commads,boolean async){
        long time1 =System.currentTimeMillis();
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        serviceOperationBean.setMessage("远程执行失败");
        com.jcraft.jsch.Session session=null;
        ChannelExec channel=null;
        try{

            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), Integer.parseInt(hostBean.getPort()));
            session.setConfig(config);
            //设置密码
            session.setPassword(hostBean.getPassword());
            //连接  超时时间30s
            session.connect(5000);
            //开启ChannelExec通道 非交互式，执行单个命令
            channel =(ChannelExec) session.openChannel("exec");

            //channel.setPty(true);
            StringBuilder sb = new StringBuilder();
            sb.append("export LANG=en;");//防止输出乱码
            if(async){
                for (String commad : commads) {
                    sb.append(commad);
                    sb.append(";");
                }
                sb.deleteCharAt(sb.length()-1);
            }else{
                for (String commad : commads) {
                    sb.append(commad);
                    sb.append(" && ");
                }
                //删除最后两个&&
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(sb.length()-1);
            }
            log.info("执行命令：{}",sb.toString());
            channel.setCommand(sb.toString());
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream otdIn = channel.getInputStream();
           // java.io.InputStream errIn= channel.getErrStream();
            //通道连接 超时时间3s
            channel.connect(5000);

            List<String> stdOuts=new ArrayList<>();
            StringBuilder stdout = new StringBuilder("");
            byte[] tmp = new byte[1024];
            while (true) {
                if (channel.isClosed()) {
                    if (otdIn.available() > 0) continue;
                    break;
                }
                if (otdIn.available() > 0) {
                    int i = otdIn.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String line=new String(tmp, 0, i);
                    stdOuts.add(line);
                    stdout.append(line);
                    stdout.append("\n");
                    log.info(line);
                }
                try{Thread.sleep(1000);}catch(Exception ee){}

            }
            //读取结果
            int exitStatus=channel.getExitStatus() ;
            serviceOperationBean.setMessage("远程执行成功,退出代码"+exitStatus);
            serviceOperationBean.setStdOuts(stdOuts);
            serviceOperationBean.setStdOut(stdout.toString());
            serviceOperationBean.setStatus("success");
        }catch (Exception ex){
            log.error("Linux 远程{} 异常{}",hostBean.getHost_ip(),ex.getMessage());
            serviceOperationBean.setMessage("远程执行异常："+ex.getMessage());
        }finally {
            try{
                if(session!=null){
                    session.disconnect();
                }
            }catch (Exception ex){
            }
            try{
                if(channel!=null){
                    channel.disconnect();
                }
            }catch (Exception ex){
            }

        }
        long time2 =System.currentTimeMillis();
        System.out.println("耗时"+(time2-time1));
        return serviceOperationBean;
    }

    public static ServiceOperationBean execByJSCH2(HostBean hostBean, List<String> commads,boolean async){
        long time1 =System.currentTimeMillis();
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        serviceOperationBean.setMessage("远程执行失败");
        com.jcraft.jsch.Session session=null;
        ChannelExec channel=null;
        try{

            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no"); //无需主机密钥确认
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), Integer.parseInt(hostBean.getPort()));
            session.setConfig(config);//设置配置
            session.setPassword(hostBean.getPassword());//设置密码
            session.connect(5000);//连接到服务器  超时时间30s

            //开启ChannelExec通道 非交互式，执行单个命令
            channel =(ChannelExec) session.openChannel("exec");

            //channel.setPty(true);
            StringBuilder sb = new StringBuilder();
            sb.append("export LANG=en;");//防止输出乱码
            if(async){
                for (String commad : commads) {
                    sb.append(commad);
                    sb.append(";");
                }
                sb.deleteCharAt(sb.length()-1);
            }else{
                for (String commad : commads) {
                    sb.append(commad);
                    sb.append(" && ");
                }
                //删除最后两个&&
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(sb.length()-1);
            }
            log.info("执行命令：{}",sb.toString());
            channel.setCommand(sb.toString());



            InputStream otdIn = channel.getInputStream();
            // java.io.InputStream errIn= channel.getErrStream();
            //通道连接 超时时间3s
            channel.connect(5000);

            List<String> stdOuts=new ArrayList<>();
            StringBuilder stdout = new StringBuilder("");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(otdIn))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stdOuts.add(line);
                    stdout.append(line);
                    stdout.append("\n");
                    log.info(line);
                }
            }
            channel.disconnect();//关闭通道

            //读取结果
            int exitStatus=channel.getExitStatus() ;
            serviceOperationBean.setMessage("远程执行成功,退出代码"+exitStatus);
            serviceOperationBean.setStdOuts(stdOuts);
            serviceOperationBean.setStdOut(stdout.toString());
            serviceOperationBean.setStatus("success");
        }catch (Exception ex){
            log.error("Linux 远程{} 异常{}",hostBean.getHost_ip(),ex.getMessage());
            serviceOperationBean.setMessage("远程执行异常："+ex.getMessage());
        }finally {
            try{
                if(session!=null){
                    session.disconnect();
                }
            }catch (Exception ex){
            }
            try{
                if(channel!=null){
                    channel.disconnect();
                }
            }catch (Exception ex){
            }

        }
        long time2 =System.currentTimeMillis();
        System.out.println("耗时"+(time2-time1));
        return serviceOperationBean;
    }


    /**
     * 从本地上传文件到Linux服务器中
     * @param hostBean
     * @param local_file
     * @param remote_file
     * @return
     */
    public static ServiceOperationBean uploadByJSCH(HostBean hostBean,String local_file,String remote_file){
        long time1 =System.currentTimeMillis();
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        serviceOperationBean.setMessage("远程执行失败");
        com.jcraft.jsch.Session session=null;
        ChannelSftp channel=null;
        try{

            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), Integer.parseInt(hostBean.getPort()));
            session.setConfig(config);
            //设置密码
            session.setPassword(hostBean.getPassword());
            //连接  超时时间30s
            session.connect(15000);
            //开启ChannelExec通道 非交互式，执行单个命令
            channel =(ChannelSftp) session.openChannel("sftp");
            //通道连接 超时时间3s
            channel.connect(25000);

            //channel.put("D:\\test\\zxx_dump.zip","/software");  直接将本地文件复制到/software中 ，如果文件中已存在直接覆盖
            //channel.put("D:\\test\\sftptest.txt","/software/sftptest2.txt");  复制并重命名
            channel.put(local_file,remote_file);

            //读取结果
            int exitStatus=channel.getExitStatus() ;
            serviceOperationBean.setMessage("远程执行成功,退出代码"+exitStatus);

            serviceOperationBean.setStatus("success");
        }catch (Exception ex){
            log.error("Linux 远程{} 异常{}",hostBean.getHost_ip(),ex.getMessage());
            serviceOperationBean.setMessage("远程执行异常："+ex.getMessage());
        }finally {
            try{
                if(channel!=null){
                    channel.exit();
                    channel.disconnect();
                }
            }catch (Exception ex){
            }
            try{
                if(session!=null){
                    session.disconnect();
                }
            }catch (Exception ex){
            }


        }
        long time2 =System.currentTimeMillis();
        System.out.println("耗时"+(time2-time1));
        return serviceOperationBean;
    }
    public static ServiceOperationBean uploadByJSCH(HostBean hostBean,InputStream in,String remote_file){
        long time1 =System.currentTimeMillis();
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        serviceOperationBean.setMessage("远程执行失败");
        com.jcraft.jsch.Session session=null;
        ChannelSftp channel=null;
        try{

            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), Integer.parseInt(hostBean.getPort()));
            session.setConfig(config);
            //设置密码
            session.setPassword(hostBean.getPassword());
            //连接  超时时间30s
            session.connect(15000);
            //开启ChannelExec通道 非交互式，执行单个命令
            channel =(ChannelSftp) session.openChannel("sftp");
            //通道连接 超时时间3s
            channel.connect(25000);

            //channel.put("D:\\test\\zxx_dump.zip","/software");  直接将本地文件复制到/software中 ，如果文件中已存在直接覆盖
            //channel.put("D:\\test\\sftptest.txt","/software/sftptest2.txt");  复制并重命名
            channel.put(in,remote_file);

            //读取结果
            int exitStatus=channel.getExitStatus() ;
            serviceOperationBean.setMessage("远程执行成功,退出代码"+exitStatus);

            serviceOperationBean.setStatus("success");
        }catch (Exception ex){
            log.error("Linux 远程{} 异常{}",hostBean.getHost_ip(),ex.getMessage());
            serviceOperationBean.setMessage("远程执行异常："+ex.getMessage());
        }finally {
            try{
                if(channel!=null){
                    channel.exit();
                    channel.disconnect();
                }
            }catch (Exception ex){
            }
            try{
                if(session!=null){
                    session.disconnect();
                }
            }catch (Exception ex){
            }


        }
        long time2 =System.currentTimeMillis();
        System.out.println("耗时"+(time2-time1));
        return serviceOperationBean;
    }
    /**
     * 从Linux服务器中下载文件
     * @param hostBean
     * @param source
     * @param destination
     * @return
     */
    public static ServiceOperationBean downloadByJSCH(HostBean hostBean,String source,String destination){
        long time1 =System.currentTimeMillis();
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        serviceOperationBean.setMessage("远程执行失败");
        com.jcraft.jsch.Session session=null;
        ChannelSftp channel=null;
        try{

            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), Integer.parseInt(hostBean.getPort()));
            session.setConfig(config);
            //设置密码
            session.setPassword(hostBean.getPassword());
            //连接  超时时间30s
            session.connect(15000);
            //开启ChannelExec通道 非交互式，执行单个命令
            channel =(ChannelSftp) session.openChannel("sftp");
            //通道连接 超时时间3s
            channel.connect(25000);


            //channel.get("/software/sftptest2.txt","D:\\test\\sftptest.txt",);  复制并重命名
            channel.get(source,destination);
            //读取结果
            int exitStatus=channel.getExitStatus() ;
            serviceOperationBean.setMessage("远程执行成功,退出代码"+exitStatus);

            serviceOperationBean.setStatus("success");
        }catch (Exception ex){
            log.error("Linux 远程{} 异常{}",hostBean.getHost_ip(),ex.getMessage());
            serviceOperationBean.setMessage("远程执行异常："+ex.getMessage());
        }finally {
            try{
                if(session!=null){
                    session.disconnect();
                }
            }catch (Exception ex){
            }
            try{
                if(channel!=null){
                    channel.disconnect();
                }
            }catch (Exception ex){
            }

        }
        long time2 =System.currentTimeMillis();
        System.out.println("耗时"+(time2-time1));
        return serviceOperationBean;
    }


    public static ServiceOperationBean readByJSCH(HostBean hostBean,String source,String encode){
        //适用于读取小文件
        long time1 =System.currentTimeMillis();
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        serviceOperationBean.setMessage("远程执行失败");
        com.jcraft.jsch.Session session=null;
        ChannelSftp channel=null;
        try{

            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), Integer.parseInt(hostBean.getPort()));
            session.setConfig(config);
            //设置密码
            session.setPassword(hostBean.getPassword());
            //连接  超时时间30s
            session.connect(15000);
            //开启ChannelExec通道 非交互式，执行单个命令
            channel =(ChannelSftp) session.openChannel("sftp");
            //通道连接 超时时间3s
            channel.connect(25000);

            InputStream inputStream=channel.get(source);
            List<String> stdOuts=new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,encode))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stdOuts.add(line);
                    log.info(line);
                }
            }
            channel.disconnect();//关闭通道
            //读取结果
            int exitStatus=channel.getExitStatus() ;
            serviceOperationBean.setMessage("远程执行成功,退出代码"+exitStatus);
            serviceOperationBean.setStdOuts(stdOuts);
            serviceOperationBean.setStatus("success");
        }catch (Exception ex){
            log.error("Linux 远程{} 异常{}",hostBean.getHost_ip(),ex.getMessage());
            serviceOperationBean.setMessage("远程执行异常："+ex.getMessage());
        }finally {
            try{
                if(session!=null){
                    session.disconnect();
                }
            }catch (Exception ex){
            }
            try{
                if(channel!=null){
                    channel.disconnect();
                }
            }catch (Exception ex){
            }

        }
        long time2 =System.currentTimeMillis();
        System.out.println("耗时"+(time2-time1));
        return serviceOperationBean;
    }

    public static ServiceOperationBean writeByJSCH(HostBean hostBean,String destination,byte[] content){
        long time1 =System.currentTimeMillis();
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        serviceOperationBean.setMessage("远程执行失败");
        com.jcraft.jsch.Session session=null;
        ChannelSftp channel=null;
        try{

            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(hostBean.getUser_name(), hostBean.getHost_ip(), Integer.parseInt(hostBean.getPort()));
            session.setConfig(config);
            //设置密码
            session.setPassword(hostBean.getPassword());
            //连接  超时时间30s
            session.connect(15000);
            //开启ChannelExec通道 非交互式，执行单个命令
            channel =(ChannelSftp) session.openChannel("sftp");
            //通道连接 超时时间3s
            channel.connect(25000);
            //远程写入
            OutputStream outputStream=channel.put(destination);
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();

            channel.disconnect();//关闭通道
            //读取结果
            int exitStatus=channel.getExitStatus() ;
            serviceOperationBean.setMessage("远程执行成功,退出代码"+exitStatus);
            serviceOperationBean.setStatus("success");
        }catch (Exception ex){
            log.error("Linux 远程{} 异常{}",hostBean.getHost_ip(),ex.getMessage());
            serviceOperationBean.setMessage("远程执行异常："+ex.getMessage());
        }finally {
            try{
                if(session!=null){
                    session.disconnect();
                }
            }catch (Exception ex){
            }
            try{
                if(channel!=null){
                    channel.disconnect();
                }
            }catch (Exception ex){
            }

        }
        long time2 =System.currentTimeMillis();
        System.out.println("耗时"+(time2-time1));
        return serviceOperationBean;
    }

    public static void main(String[] args){
        HostBean hostBean=new HostBean();
        hostBean.setHost_ip("172.17.112.121");
        hostBean.setUser_name("root");
        hostBean.setPort("22");
        hostBean.setPassword("wiscom");

        List<String> commads=new ArrayList<>();
//        commads.add("cd /psmp/psmp_AllToDB-postgresql");
//        commads.add("cd /psmp/psmp_AllToDB-postgresql");
        //cd /psmp/psmp_AllToDB-postgresql && nohup java -Xms100m -Xmx3000m -jar psmp_AllToDB-postgresql.jar >/dev/null 2>&1  &
        //执行以下命令，会在while循环中卡住
        commads.add("cd /psmp/psmp_AllToDB-postgresql");
        commads.add("nohup java -Xms100m -Xmx3000m -jar psmp_AllToDB-postgresql.jar >start.log 2>&1  &  ");
//        commads.add("tail -200  /psmp/psmp_AllToDB-postgresql/log/psmp_AllToDB.log | base64");
        ServiceOperationBean serviceOperationBean=exec(hostBean,commads,true);
//        ServiceOperationBean serviceOperationBean=downloadByJSCH(hostBean,"/software/sftptest2.txt","D:\\test\\sftptest.txt");
//        System.out.println(ObjectToJsonUtil.toJson(serviceOperationBean));
    }
}
