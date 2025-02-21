package com.boot.util.common;


import com.boot.bean.HostBean;
import com.boot.bean.ServiceOperationBean;
import org.apache.http.client.config.AuthSchemes;
import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;
import org.apache.http.client.config.AuthSchemes;
import org.sentrysoftware.winrm.WinRMHttpProtocolEnum;
import org.sentrysoftware.winrm.WindowsRemoteCommandResult;
import org.sentrysoftware.winrm.command.WinRMCommandExecutor;
import org.sentrysoftware.winrm.service.client.auth.AuthenticationEnum;

import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;
import java.util.List;

public class WindowRMUtil {
    public static ServiceOperationBean exec(HostBean hostBean, String command){
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();
        WinRmClientContext context = WinRmClientContext.newInstance();

        try{
            WinRmTool.Builder builder = WinRmTool.Builder.builder(hostBean.getHost_ip(), hostBean.getUser_name(), hostBean.getPassword());
            builder.setAuthenticationScheme(AuthSchemes.NTLM);
            builder.port(5985);
            builder.useHttps(false);
            builder.context(context);
            builder.disableCertificateChecks(true);

            WinRmTool tool = builder.build();
//            tool.setOperationTimeout(5l);
//            tool.setConnectionTimeout(5l);
            tool.setRetriesForConnectionFailures(1); //连接尝试次数
            WinRmToolResponse resp;

            resp=tool.executePs(command); //执行powershell脚本

            //如果网络异常以及无法远程操作 执行不到下面代码
            if(resp.getStatusCode()==0){
                serviceOperationBean.setStatus("success");
                serviceOperationBean.setStdOut(resp.getStdOut()); //输出resp
            }

        }catch(Exception ex){
            ex.printStackTrace();
            serviceOperationBean.setMessage("服务器远程失败，请检查网络或winrm配置！");
        }finally {
            context.shutdown();
        }
        return serviceOperationBean;
    }
    public static ServiceOperationBean execByWinrm(HostBean hostBean,String command){
        ServiceOperationBean serviceOperationBean=new ServiceOperationBean();


        try{
            final long timeout = 50 * 1000L; // in milliseconds
            List<AuthenticationEnum> authentications=new ArrayList<>();
            authentications.add(AuthenticationEnum.NTLM);
            WindowsRemoteCommandResult windowsRemoteCommandResult=WinRMCommandExecutor.execute(command,WinRMHttpProtocolEnum.HTTP,hostBean.getHost_ip(),Integer.parseInt(hostBean.getPort()),hostBean.getUser_name(),hostBean.getPassword().toCharArray(),null,timeout,null,null,authentications);
            System.out.println(windowsRemoteCommandResult.getStdout());

        }catch(Exception ex){
            ex.printStackTrace();
            serviceOperationBean.setMessage("服务器远程失败，请检查网络或winrm配置！");
        }finally {

        }
        return serviceOperationBean;
    }
    public static void main(String[] args){
        HostBean hostBean=new HostBean();
        hostBean.setHost_ip("172.17.112.251");
        hostBean.setUser_name("administrator");
        hostBean.setPort("5985");
        hostBean.setPassword("wiscom123!");

        String commnd="echo wiscom";
        ServiceOperationBean serviceOperationBean=execByWinrm(hostBean,commnd);
//        System.out.println(ObjectToJsonUtil.toJson(serviceOperationBean));
    }
}
