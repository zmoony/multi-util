package com.boot.util.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Function;

/**
 * 占位符解析器
 */
public class PlaceholderHelper {
    /**
     * 默认前缀占位符
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * 默认后缀占位符
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * 占位符前缀长度
     */
    public static final int PREFIX_LENGTH = DEFAULT_PLACEHOLDER_PREFIX.length();

    /**
     * 占位符后缀长度
     */
    public static final int SUFFIX_LENGTH = DEFAULT_PLACEHOLDER_SUFFIX.length();

    /**
     * 根据map解析占位符
     *
     * @param content  内容
     * @param valueMap map
     * @return 解析后的内容
     */
    public static String resolve(String content, Map<String, Object> valueMap) {
        return resolveByRule(content, placeholder -> {
            String[] fieldNames = placeholder.split("\\.");
            //如果是多级占位符 如：user.name 那么可能是map中的key，也可能是对象中的属性 优先取map中的key 如果没有则取对象中的属性
            if (fieldNames.length > 1) {
                //先取map中的key
                Object value = valueMap.get(placeholder);
                if (null != value) {
                    return String.valueOf(value);
                }

                //获取根对象 如：user.name 那么先取user对象
                Object obj = valueMap.get(fieldNames[0]);
                return String.valueOf(getFieldValue(fieldNames, obj));
            }
            return String.valueOf(valueMap.get(placeholder));
        });
    }


    /**
     * 根据对象解析占位符
     *
     * @param content 内容
     * @param obj     对象
     * @return 解析后的内容
     */
    public static String resolve(String content, Object obj) {
        if (null == content || null == obj) {
            return content;
        }

        return resolveByRule(content, placeholder -> {
            String[] fieldNames = placeholder.split("\\.");

            //获取根对象 如：user.name 那么先取user对象
            return String.valueOf(getFieldValue(fieldNames, obj));
        });
    }

    /**
     * 获取对象中字段的值
     *
     * @param fieldNames 字段名数组
     * @param obj        对象
     * @return 属性值
     */
    private static Object getFieldValue(String[] fieldNames, Object obj) {
        //适配直接根据对象中的属性获取值 如：name 实际上是取user.name 但是必须从根对象开始取
        if (fieldNames.length == 1) {
            return getFieldValue(obj, fieldNames[0]);
        }

        //取对象中的属性 如：user.name 那么先取user对象，然后取name属性
        for (int i = 1; i < fieldNames.length; i++) {
            //获取属性值
            Object fieldValue = getFieldValue(obj, fieldNames[i]);
            //如果属性值为空，则返回null字符串
            if (null == fieldValue) {
                return "null";
            }
            //如果属性值不为空，则将属性值赋值给objClass
            obj = fieldValue;
        }

        return obj;
    }

    /**
     * 获取对象中字段的值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 属性值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (null == obj || null == fieldName) {
            return null;
        }

        //多级Map嵌套的情况 如：userMap.detailMap.name 那么先取userMap，然后取detailMap，最后取name属性
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).get(fieldName);
        }

        //获取字段
        Field field = getField(obj.getClass(), fieldName);
        //获取字段的值
        return getFieldValue(field, obj);
    }

    /**
     * 用于获取对象中字段的值
     *
     * @param field 字段
     * @param obj   对象
     * @return 属性值
     */
    public static Object getFieldValue(Field field, Object obj) {
        if (null == field) {
            return null;
        }

        //将字段设置为可访问 就是将private修饰符的字段也能访问
        makeAccessible(field);

        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从类中获取字段
     *
     * @param clazz     类
     * @param fieldName 字段名
     * @return
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        if (null == clazz || null == fieldName || fieldName.trim().isEmpty()) {
            return null;
        }

        // 获取所有属性
        while (clazz != Object.class && clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (fieldName.equals(declaredField.getName())) {
                    return declaredField;
                }
            }
            // 获取父类 递归查找
            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /**
     * 根据规则解析占位符
     *
     * @param content 内容
     * @param rule    规则 回调函数
     * @return 解析后的内容
     */
    public static String resolveByRule(String content, Function<String, String> rule) {
        // 获取第一个占位符的位置开始
        int start = content.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        if (start == -1) {
            return content;
        }

        // 用于替换占位符的内容
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            //从初始占位符开始查找结束符
            int end = result.indexOf(DEFAULT_PLACEHOLDER_SUFFIX, start);
            //获取占位符属性值，如${id}, 即获取id
            String placeholder = result.substring(start + PREFIX_LENGTH, end);
            //替换整个占位符内容，即将${id}值替换为替换规则回调中的内容
            String replaceContent = placeholder.trim().isEmpty() ? "" : rule.apply(placeholder);
            result.replace(start, end + SUFFIX_LENGTH, replaceContent);
            //从替换后的内容中向后继续查找占位符
            start = result.indexOf(DEFAULT_PLACEHOLDER_PREFIX, start + replaceContent.length());
        }

        return result.toString();
    }

    /**
     * 设置字段为可见
     *
     * @param field 字段
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }
}
