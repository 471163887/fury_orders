package com.brilliant.fury.core.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

/**
 * 常用的字符串及Guava的操作
 *
 * @author fury.
 * version 2017/9/13.
 */
public class GuavaUtil {

    public static final String COMMAS = ",";

    public static final String COLON = ";";

    public static final String VERTICAL = "|";

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final Splitter COMMAS_SPLITTER = Splitter.on(COMMAS).omitEmptyStrings()
        .trimResults();

    public static final Splitter COMMAS_ALLOW_EMPTY = Splitter.on(COMMAS).trimResults();

    public static final Joiner COMMAS_JOINER = Joiner.on(COMMAS).skipNulls();

    public static final Splitter COLON_SPLITTER = Splitter.on(COLON).omitEmptyStrings()
        .trimResults();

    public static final Joiner COLON_JOINER = Joiner.on(COLON).skipNulls();

    public static final Splitter VERTICAL_SPLITTER = Splitter.on(VERTICAL).omitEmptyStrings()
        .trimResults();

    public static final Joiner VERTICAL_JOINER = Joiner.on(VERTICAL).skipNulls();

    public static final String EMPTY_STRING = "";

    private static final String NULL = "null";

    private static final String UNDEFINED = "undefined";

    /**
     * is null or empty.
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        String checkString = str.trim().toLowerCase();
        return checkString.equals(EMPTY_STRING) || checkString.equals(NULL)
            || checkString.equals(UNDEFINED);
    }

    /**
     * not null or empty.
     */
    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * default if str empty.
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        if (isEmpty(str)) {
            return defaultStr;
        }
        return str;
    }

    /**
     * default if integer empty.
     */
    public static Integer defaultIntIfEmpty(Integer target, Integer defaultInt) {
        if (null == target) {
            return defaultInt;
        }
        return target;
    }

    /**
     * default if String empty.
     */
    public static Integer defaultIntIfStrEmpty(String target, Integer defaultInt) {
        try {
            if (isEmpty(target)) {
                return defaultInt;
            }
            return Integer.parseInt(target);
        } catch (Exception e) {
            return defaultInt;
        }
    }

    public static String genMurHash(String str) {
        HashCode hashCode = Hashing.murmur3_128().hashString(str, StandardCharsets.UTF_8);
        return hashCode.toString();
    }

}
