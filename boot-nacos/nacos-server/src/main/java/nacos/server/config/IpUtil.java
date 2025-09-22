package nacos.server.config;

import com.alibaba.nacos.spring.util.Tuple;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IpUtil
 *
 * @author yuez
 * @since 2023/4/4
 */
public class IpUtil {
    /**
     * 不传参数，直接通过ServletRequestAttributes获取当前项目所在服务器的端口号和ip地址
     * 需要项目完全启动
     * @return
     */
    public static Tuple<String,Integer> getUrlByServletRequestAttributes(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String localAddr = request.getLocalAddr();
        int port = request.getServerPort();
        return Tuple.of(localAddr,port);
    }

    /**
     * 通过 HttpServletRequest 传参数获取当前项目所在服务器的端口号和ip地址
     * 需要项目完全启动
     * @param request
     * @return
     */
    public static Tuple<String,Integer> getUrlByHttpServletRequest(HttpServletRequest request){
        String localAddr = request.getLocalAddr();
        int port = request.getServerPort();
        return Tuple.of(localAddr,port);
    }

    /**
     * Java获取当前系统的ip地址，端口号从配置文件读取
     * @param port
     * @return
     * @throws UnknownHostException
     */
    public static Tuple<String,Integer> getUrlByInetAddress(int port) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        return Tuple.of(localHost.getHostAddress(),port);
    }
}
