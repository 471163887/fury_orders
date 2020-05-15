package com.brilliant.fury.core.util;

import com.brilliant.fury.core.base.JsonUtil;
import com.google.common.collect.Maps;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于处理Json时，驼峰格式和下滑线格式的转换
 *
 * @author by fury.
 * version 2018/1/16.
 */
public class UnderlineUtil {

    private static final String A_ZA_Z_D = "([A-Za-z\\d]+)(_)?";
    private static final String A_Z_A_Z_D = "[A-Z]([a-z\\d]+)?";
    private static final String CLASS = "class";

    /**
     * 将一个 Java 对象转换成 json
     * 并且将内部变量的驼峰格式，换成下滑线格式。
     */
    public static String camel2UnderlineJson(Object obj) {
        Map<String, Object> stringObjectMap = bean2Map(obj, true);
        return JsonUtil.toJson(stringObjectMap);
    }

    public static Map<String, Object> bean2Map(Object obj, boolean toUnderLine) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals(CLASS)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (toUnderLine) {
                        key = camel2Underline(key);
                    } else {
                        key = underline2Camel(key, true);
                    }
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 是否为小驼峰
     *                   小驼峰: youAreAwesome
     *                   大驼峰: YouAreAwesome
     * @return 转换后的字符串
     */
    private static String underline2Camel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(A_ZA_Z_D);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(
                word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    private static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(A_Z_A_Z_D);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

}
