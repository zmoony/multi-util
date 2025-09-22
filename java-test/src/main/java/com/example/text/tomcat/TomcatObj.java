package com.example.text.tomcat;

import com.example.text.TextApplication;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletContextInitializerBeans;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.Collection;


/**
 * TomcatObj
 * 自定义加载tomcat 运行
 *
 * @author yuez
 * @since 2024/8/21
 */
public class TomcatObj {

    public static void main(String[] args) {

    }

    public static void startTomcatServer() {
        System.out.println("启动tomcat");
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector();
        tomcat.getHost();
        Context context = tomcat.addWebapp("/", "");
        //设置servlet.并设置名称传递给下面的路劲映射
        tomcat.addServlet("/", "index", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.getWriter().append("hello");
            }
        });
        //将所有请求路径（解码后的"/"）映射到名为"index"的Servlet上。
        // 这意味着任何进入根路径的HTTP请求都会被转发给"index"这个Servlet处理。
        context.addServletMappingDecoded("/", "index");
        try {
            tomcat.init();
            tomcat.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 应该TextApplication里编码
     * SpringBoot单体应用真正的零停机无缝更新代码
     * 判断端口是否占用
     * 占用则先通过其他端口启动
     * 等待启动完毕后终止老进程
     * 重新创建容器实例并且关联DispatcherServlet
     */
    public static void startSpringBootServer(String[] args) {
        System.out.println("启动springboot");
        String[] newArgs = args.clone();
        int defaultPort = 8888;
        boolean needChangePort = false;
        if (isPortInUse(defaultPort)) {
            //被占用，启用其他端口启动
            newArgs = new String[args.length + 1];
            System.arraycopy(args, 0, newArgs, 0, args.length);
            newArgs[newArgs.length - 1] = "--server.port=9000";
            needChangePort = true;
        }
        //启动类
        ConfigurableApplicationContext run = SpringApplication.run(TextApplication.class, newArgs);
        if (needChangePort) {
            //构建一个命令字符串command，使用lsof命令来查找正在使用defaultPort端口的进程，并通过grep、awk和xargs命令组合杀死该进程。
            // 命令字符串被格式化为"lsof -i :<defaultPort> | grep LISTEN | awk '{print $2}' | xargs kill -9"。
            String command = String.format("lsof -i :%d | grep LISTEN | awk '{print $2}' | xargs kill -9", defaultPort);
            try {
                //使用Runtime.getRuntime().exec()方法执行构建好的命令字符串，并调用waitFor()方法等待命令执行完成。
                Runtime.getRuntime().exec(new String[]{"sh", "-c", command}).waitFor();
                while (isPortInUse(defaultPort)) {
                }
                //重新启动
                //调用getWebServerFactory()方法获取ServletWebServerFactory实例，
                // 并将其转换为TomcatServletWebServerFactory类型，然后设置端口号为defaultPort。
                ServletWebServerFactory webServerFactory = getWebServerFactory(run);
                ((TomcatServletWebServerFactory) webServerFactory).setPort(defaultPort);
                //调用invokeSelfInitialize()方法对run对象进行初始化，
                // 并将初始化后的对象作为参数传递给webServerFactory.getWebServer()方法获取WebServer实例。
                WebServer webServer = webServerFactory.getWebServer(invokeSelfInitialize(((ServletWebServerApplicationContext) run)));
                webServer.start();
            //最后调用run.getWebServer().stop()停止原有的WebServer实例
                ((ServletWebServerApplicationContext) run).getWebServer().stop();
            } catch (IOException | InterruptedException ignored) {
            }


        }
    }

    /**
     * 利用反射获取ServletWebServerApplicationContext的getSelfInitializer方法
     * @param context
     * @return
     */
    private static ServletContextInitializer invokeSelfInitialize(ServletWebServerApplicationContext context) {
        try {
            Method method = ServletWebServerApplicationContext.class.getDeclaredMethod("getSelfInitializer");
            method.setAccessible(true);
            return (ServletContextInitializer) method.invoke(context);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean isPortInUse(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    protected static Collection<ServletContextInitializer> getServletContextInitializerBeans(ConfigurableApplicationContext context) {
        return new ServletContextInitializerBeans(context.getBeanFactory());
    }


    private static ServletWebServerFactory getWebServerFactory(ConfigurableApplicationContext context) {
        String[] beanNames = context.getBeanFactory().getBeanNamesForType(ServletWebServerFactory.class);

        return context.getBeanFactory().getBean(beanNames[0], ServletWebServerFactory.class);
    }


}
