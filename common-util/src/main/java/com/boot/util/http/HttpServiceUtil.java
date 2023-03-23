package com.boot.util.http;

import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * http的参数的工具类
 *
 * @author yuez
 * @version 1.0.0
 * @className HttpServiceUtil
 * @date 2021/4/12 10:12
 **/
@Log4j2
public class HttpServiceUtil {

    /**
    　　下载时设置响应流的头部信息
        <code>
             this.setResponseHeader(response, fileName);
             OutputStream os = response.getOutputStream();
             wb.write(os);
             os.flush();
             os.close();
        </code>
    * @param response
    * @param fileName
    * @throws
    * @author yuez
    * @date 2021/4/12 10:14
    */
    public void setResponseHeader(HttpServletResponse response, String fileName){
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
    　　下载文件（内部设置流）
    * @param filepath
    * @param response
    * @throws
    * @author yuez
    * @date 2021/4/12 10:15
    */
    public void downloadFile(String filepath, HttpServletResponse response) {
        try (InputStream is = new FileInputStream(filepath);) {
            File file = new File(filepath);
            String fileName = file.getName();
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setCharacterEncoding("ISO8859-1");
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[100];
            int len;
            while ((len = is.read(b)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("下载模板失败：{}", e);
        }
    }
}
