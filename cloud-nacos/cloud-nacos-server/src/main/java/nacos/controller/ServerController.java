package nacos.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ServerController
 *
 * @author yuez
 * @since 2023/4/10
 */
@RestController

public class ServerController {

    @RequestMapping(value = "/echo/{string}",method = RequestMethod.GET)
    public String echo(@PathVariable String string) {
        return "hello cloud :"+string;
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public List<NetworkConnectionBean> info() {
        String json = "[{\"password\":\"Huawei12#$\",\"totalSpace\":\"18195394.00\",\"freeSpace\":\"3812552.00\",\"percentage\":\"79.05%\",\"status\":true,\"nfs_mount_dir\":\"fjcc06\",\n" +
                "\"nfs_mount_host\":\"32.29.165.94\",\"http_port\":\"80\",\"all_path\":\"yes\",\"localName\":null,\"userName\":\"admin001\",\"remoteName\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian6\",\n" +
                "\"remotePath\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian6\"},\n" +
                "{\"password\":\"Huawei12#$\",\"totalSpace\":\"18195394.00\",\"freeSpace\":\"3812552.00\",\"percentage\":\"79.05%\",\"status\":true,\"nfs_mount_dir\":\"fjcc\",\n" +
                "\"nfs_mount_host\":\"32.29.165.94\",\"http_port\":\"80\",\n" +
                "\"all_path\":\"yes\",\"localName\":null,\"userName\":\"admin001\",\n" +
                "\"remoteName\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian\",\"remotePath\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian\"}]";

        List<NetworkConnectionBean> networkConnectionBeans = new ArrayList<>();
        NetworkConnectionBean bean1 = new NetworkConnectionBean();
        bean1.setPercentage("79.05%");
        bean1.setNfs_mount_dir("fjcc06");
        networkConnectionBeans.add(bean1);

        NetworkConnectionBean bean2 = new NetworkConnectionBean();
        bean2.setPercentage("90.05%");
        bean2.setNfs_mount_dir("fjcc01");
        networkConnectionBeans.add(bean2);


        return networkConnectionBeans;
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public String info2() {
        String json = "[{\"password\":\"Huawei12#$\",\"totalSpace\":\"18195394.00\",\"freeSpace\":\"3812552.00\",\"percentage\":\"79.05%\",\"status\":true,\"nfs_mount_dir\":\"fjcc06\",\n" +
                "\"nfs_mount_host\":\"32.29.165.94\",\"http_port\":\"80\",\"all_path\":\"yes\",\"localName\":null,\"userName\":\"admin001\",\"remoteName\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian6\",\n" +
                "\"remotePath\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian6\"},\n" +
                "{\"password\":\"Huawei12#$\",\"totalSpace\":\"18195394.00\",\"freeSpace\":\"3812552.00\",\"percentage\":\"79.05%\",\"status\":true,\"nfs_mount_dir\":\"fjcc\",\n" +
                "\"nfs_mount_host\":\"32.29.165.94\",\"http_port\":\"80\",\n" +
                "\"all_path\":\"yes\",\"localName\":null,\"userName\":\"admin001\",\n" +
                "\"remoteName\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian\",\"remotePath\":\"\\\\\\\\jn.jngacifs.cn\\\\tupian\"}]";
        return json;
    }

    @RequestMapping(value = "/getNewInsertTime",method = RequestMethod.GET)
    public Map<String,Long> time() {
        Map<String,Long> map = new HashMap<>();
        map.put("insertTime",1700115445686L);
        return map;

    }


}

@Data
class NetworkConnectionBean {
    private String RemoteName;
    private String LocalName;
    private String UserName;
    private String password;
    private String RemotePath;

    private String totalSpace;
    private String freeSpace;
    private String percentage;
    private boolean status=false; //华为云储存可能90%就无法写入

    private String nfs_mount_dir;//nfs目录挂载点
    private String nfs_mount_host;//nfs 挂载服务器

    private String http_port;
    private String all_path;


}
