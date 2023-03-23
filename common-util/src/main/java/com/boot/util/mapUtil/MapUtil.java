package com.boot.util.mapUtil;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuez
 * @title: MapUtil
 * @projectName zy_wiscom
 * @description: map的处理
 * @date 2021/2/22 14:17
 */
public class MapUtil {

    public static final String REGEX = "\\d{4}-\\d{1,2}-\\d{1,2}T\\d{2}:\\d{2}:\\d{2}.*";
    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final char UNDERLINE = '_';

    public static Map<String, Object> formatHumpName(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        Pattern p = Pattern.compile(REGEX);
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = underlineToCamel(key);
            Object value = entry.getValue();
            Matcher matcher = p.matcher(String.valueOf(value));
            if(matcher.find()){
                String date = String.valueOf(value);
                DateTime dateTime = new DateTime(date);
                Date date1 = dateTime.toDate();
                DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
                String dateStr = dateFormat.format(date1);
                newMap.put(newKey, dateStr);
            }else{
                newMap.put(newKey, entry.getValue());
            }
        }
        return newMap;
    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(Character.toLowerCase(param.charAt(i)));
            }
        }
        return sb.toString();
    }
    /**
     * 将List中map的key值命名方式格式化为驼峰
     *
     * @param
     * @return
     */
    public static List<Map<String, Object>> formatHumpNameForList(List<Map<String, Object>> list) {
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> o : list) {
            newList.add(formatHumpName(o));
        }
        return newList;
    }

}
