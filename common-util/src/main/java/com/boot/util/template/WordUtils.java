package com.boot.util.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

/**
 * word导出工具类
 *
 * @author Lgd
 */
@Slf4j
public class WordUtils {
    private static WordUtils service = null;

    public WordUtils() {
        super();
    }

    public static WordUtils getInstance() {
        if (service == null) {
            service = new WordUtils();
        }
        return service;
    }

    /**
     * 导出至word
     *
     * @param ftl      ftl模板地址
     * @param fileName 文件名
     * @param dataMap  导出数据
     * @param response response
     */
    public void exportDocFile(String ftl, String fileName, Map<String, Object> dataMap, HttpServletResponse response) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("UTF-8");

        OutputStreamWriter oWriter = null;
        Writer out = null;
        try {
            ClassPathResource resource = new ClassPathResource(ftl);
            File file = File.createTempFile("StatementTemp", ".ftl");
            FileUtils.copyURLToFile(resource.getURL(), file);
            String templateFile = file.getAbsolutePath();
            templateFile = pathReplace(templateFile);
            if (null == templateFile) {
                log.error("导出失败，模板不存在");
                return;
            }
            String ftlPath = templateFile.substring(0, templateFile.lastIndexOf("/"));
            log.info("模板路径：{}", ftlPath);
            // FTL文件所存在的位置--绝对路径
            configuration.setDirectoryForTemplateLoading(new File(ftlPath));
            //configuration.setClassForTemplateLoading(this.getClass(), ftlPath);
            String ftlFile = templateFile.substring(templateFile.lastIndexOf("/") + 1);
            log.info("模板临时文件名称：{}", ftlFile);
            // 模板文件名
            Template template = configuration.getTemplate(ftlFile);

            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            String filename = fileName + ".doc";
            response.setHeader("Content-Disposition", "attachment; filename=" + urlEncode(filename));
            oWriter = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            out = new BufferedWriter(oWriter);
            template.process(dataMap, out);
        } catch (Exception e) {
            log.error("导出word失败：", e);
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != oWriter) {
                    oWriter.close();
                }
            } catch (IOException e) {
                log.error("Writer or OutputStreamWriter close failed:{}", e.getMessage());
            }
        }
    }

    /**
     * 创建doc文件
     *
     * @param ftl        src/main/resources/ftl/xxx.ftl
     * @param dataMap    导出数据
     * @param exportPath eg: /tmp/test/test123.doc
     * @param loadType   设置路径加载方式。1-绝对路径，2-项目相对路径
     * @return {@link File}
     * @ver v1.0.0
     */
    public File createDocFile(String ftl, Map<String, Object> dataMap, String exportPath, int loadType) {
        File file = new File(ftl);
        String templateFile = file.getAbsolutePath();
        Template t = null;
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("UTF-8");
        try {
            templateFile = pathReplace(templateFile);
            if (null == templateFile) {
                log.error("模板不存在！");
                return null;
            }
            String ftlPath = templateFile.substring(0, templateFile.lastIndexOf("/"));
            if (loadType == 1) {
                // FTL文件所存在的位置
                configuration.setDirectoryForTemplateLoading(new File(ftlPath));
            } else {
                //以类加载的方式查找模版文件路径
                configuration.setClassForTemplateLoading(this.getClass(), ftlPath);
            }

            String ftlFile = templateFile.substring(templateFile.lastIndexOf("/") + 1);
            // 模板文件名
            t = configuration.getTemplate(ftlFile);

            File outFile = new File(exportPath);
            Writer out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outFile.toPath())));
            t.process(dataMap, out);
        } catch (Exception e) {
            log.error("导出word文档出错：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 把路径的\替换成/
     *
     * @param path 路径
     * @return {@link String}
     * @ver v1.0.0
     */
    private static String pathReplace(String path) {
        while (path != null && path.contains("\\")) {
            path = path.replace("\\", "/");
        }
        return path;
    }

    /**
     * 图片转base64
     *
     * @param imgStr 图片 http 地址
     * @return {@link String}
     * @ver v1.0.0
     */
    public static String getImgStrToBase64(String imgStr) {
        if (StringUtils.isEmpty(imgStr)) {
            return "";
        }
        log.info("imgStr:===>{}", imgStr);
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        byte[] buffer = null;
        try {
            //判断网络链接图片文件/本地目录图片文件
            if (imgStr.startsWith("http://") || imgStr.startsWith("https://")) {
                // 创建URL
                URL url = new URL(imgStr);
                // 创建链接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                inputStream = conn.getInputStream();
                outputStream = new ByteArrayOutputStream();
                // 将内容读取内存中
                buffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                buffer = outputStream.toByteArray();
            } else {
                inputStream = Files.newInputStream(Paths.get(imgStr));
                int count = 0;
                while (count == 0) {
                    count = inputStream.available();
                }
                buffer = new byte[count];
                inputStream.read(buffer);
            }
            // 对字节数组Base64编码

            return Base64.getEncoder().encodeToString(buffer);
        } catch (Exception e) {
            log.error("图片转换失败：{}", e.getMessage());
            return "";
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    log.error("InputStream 关闭失败：{}", e.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    // 关闭outputStream流
                    outputStream.close();
                } catch (IOException e) {
                    log.error("ByteArrayOutputStream 关闭失败：{}", e.getMessage());
                }
            }
        }
    }


    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("编码失败：{}", e.getMessage());
            return "";
        }
    }
}

