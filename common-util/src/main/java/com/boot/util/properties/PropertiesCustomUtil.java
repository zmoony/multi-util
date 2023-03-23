package com.boot.util.properties;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 这个方法主要运用于热加载（修改配置文件自动加载不用重启）
 * 解析properties文件 ，properties.isEmpty()可能为true
 * 采用apache commons Configuration2组件对properties文件读和写
 * <strong>更改的target下的配置文件，在项目现场不会出现这样的问题</strong>
 * @author Administrator
 */
@Log4j2
public class PropertiesCustomUtil {
    private final Parameters params = new Parameters();
    private final FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

    public PropertiesCustomUtil(String filename) {
        //使用方法调用链为各个属性赋值
        FileBasedBuilderParameters fileBasedBuilderParameters = params.fileBased()
                .setFile(new File(filename))
                .setEncoding("UTF-8")
                .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                .setThrowExceptionOnMissing(true);
 /*       PropertiesBuilderParameters propertiesBuilderParameters = params.properties()
                .setFile(propertiesFile)
                .setEncoding("UTF-8")
                .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                .setThrowExceptionOnMissing(true);*/
        //在configure()方法中激活各个参数配置
        builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(fileBasedBuilderParameters);
        // enable auto save mode
        builder.setAutoSave(true);
        // 使用PropertiesBuilderParameters
        // .configure(propertiesBuilderParameters);
    }



    /**
     * 　　根据key获取value
     * 　　 @param property 配置文件的key
     * 　　 @return java.lang.String
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/23 17:27
     *
     */
    public String getProperty(String property) {
        String string = null;
        try {
            Configuration config = builder.getConfiguration();
            string = config.getString(property);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 　　修改文件
     * 　　 @param property key值
     * 　　 @param value 修改后的值
     * 　　 @return boolean
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/23 17:27
     *
     */
    public boolean setProperty(String property, String value) {
        boolean flag = false;
        try {
            Configuration  config = builder.getConfiguration();
            config.setProperty(property, value);
            builder.save();
            flag = true;
        } catch (Exception ex) {
            log.error("properties写入失败" + ex.getMessage());
        }
        return flag;
    }

    /**
     * 　　增加属性
     * 　　@param property key值
     * 　　 @param value 修改后的值
     * 　　 @return boolean
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/23 17:28
     *
     */
    public boolean addProperty(String property, String value) {
        boolean flag = false;
        try {
            Configuration  config = builder.getConfiguration();
            config.addProperty(property, value);
            builder.save();
            flag = true;
        } catch (ConfigurationException ex) {
            log.error("properties添加失败" + ex.getMessage());
        }
        return flag;
    }

    /**
     * 　　删除属性
     * 　　 @param property
     * 　　 @return boolean
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/23 17:28
     *
     */
    public boolean deleteProperty(String property) {
        boolean flag = false;
        try {
            Configuration  config = builder.getConfiguration();
            config.clearProperty(property);
            builder.save();
            flag = true;
        } catch (ConfigurationException ex) {
            log.error("properties添加失败" + ex.getMessage());
        }
        return flag;
    }

    /**
     * 　　将key:value转为map返回，并指定名称
     * 　　 @param key_name key的名称
     * 　　 @param value_name value的对应map的名称
     * 　　 @return java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/23 17:29
     *
     */
    public List<Map<String, String>> getList(String key_name, String value_name) {

        List<Map<String, String>> list = new ArrayList<>();
        try {
            Configuration  config = builder.getConfiguration();
            Iterator<String> keys = config.getKeys();
            while (keys.hasNext()) {
                Map<String, String> map = new HashMap<>();
                String key = keys.next();
                map.put(key_name, key);
                map.put(value_name, config.getString(key));
                list.add(map);
            }

        } catch (Exception ex) {
            log.error("properties扫描失败" + ex.getMessage());
        }
        return list;
    }

    /**
     * 　　配置文件属性全部返回
     * 　　 @param
     * 　　 @return java.util.Map<java.lang.String, java.lang.String>
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/23 17:30
     *
     */
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        try {
            Configuration  config = builder.getConfiguration();
            Iterator<String> keys = config.getKeys();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, config.getString(key));
            }

        } catch (Exception ex) {
            log.error("properties扫描失败" + ex.getMessage());
        }
        return map;
    }


    /**
     * 　　根据文件名获取配置信息,建议使用{@link #getProperty(String)}
     * 　　 @param filename
     * 　　 @return java.util.Properties
     * 　　 @throws
     * 　　 @author yuez
     * 　　 @date 2021/3/23 17:18
     */
    @Deprecated
    public static Properties getProperties(String filename) {
        Properties properties = new Properties();
        //InputStream inStream =PropertiesCustomUtil.class.getResourceAsStream(file); 这种写法的配置文件必须与此类同目录
        File file = new File(filename);
        try {
            try (InputStream inStream = new FileInputStream(file)) {
                //log.info(new File(".").getCanonicalPath()); 打印结果E:\NetBeans-workspace\psmp_OracleToKafka
                properties.load(inStream);
            }
        } catch (IOException ex) {
            log.error("Properties文件解析失败:" + ex.getMessage());
        }
        return properties;
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getTime());
    }

}

