package com.boot.util.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

/**
 * DispatcherController
 *
 * @author yuez
 * @since 2024/2/29
 */
@Log4j2
@RestController
@CrossOrigin
public class DispatcherController {

    @RequestMapping("/**")
    public void dispatcher(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String targetServiceUrl = GlobalObject.properties_business.getProperty("targetServer");
        String targetServiceUrl = "http://172.17.12.112:9500";
        URL url = new URL(targetServiceUrl + request.getRequestURI());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法、头信息等
        connection.setRequestMethod(request.getMethod());
        Enumeration<String> headerNames = request.getHeaderNames();
//        connection.setRequestProperty("resId", GlobalObject.properties_business.getProperty("resId"));
//        connection.setRequestProperty("userCredential", GlobalObject.properties_business.getProperty("userCredential"));
//        connection.setRequestProperty("appCredential", GlobalObject.properties_business.getProperty("appCredential"));

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            connection.setRequestProperty(headerName, request.getHeader(headerName));
        }

        if ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod())) {
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            InputStream is = request.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        }

        // 获取响应
        int responseCode = connection.getResponseCode();
        response.setStatus(responseCode);

        for (String headerName : connection.getHeaderFields().keySet()) {
            for (String headerValue : connection.getHeaderFields().get(headerName)) {
                response.addHeader(headerName, headerValue);
            }
        }

        if (responseCode >= 200 && responseCode < 300) {
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }

        connection.disconnect();
    }
}
