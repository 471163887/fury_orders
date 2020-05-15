package com.brilliant.fury.core.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 集合常用操作。
 * 1.判空
 * 2.分割数组
 *
 * @author by fury.
 * version 2015/8/22.
 */
public class CollectionUtil {

    /**
     * 集合为空集
     */
    public static <T> boolean isEmpty(Collection<T> c) {
        return null == c || c.size() == 0;
    }

    /**
     * 判断集合非空
     */
    public static <T> boolean isNotEmpty(Collection<T> c) {
        return null != c && c.size() > 0;
    }

    /**
     * 获取集合元素数量
     */
    public static <T> int size(Collection<T> c) {
        if (null != c) {
            return c.size();
        }
        return 0;
    }

    /**
     * 获取 map 元素数量
     */
    public static <K, V> int size(Map<K, V> map) {
        if (null != map) {
            return map.size();
        }
        return 0;
    }

    /**
     * 获取 list 的第一个元素
     */
    public static <T> T firstOrNull(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 分隔数组 根据段数分段 <多出部分在最后一个数组>
     *
     * @param data     被分隔的数组
     * @param segments 需要分隔的段数
     */
    public <T> List<List<T>> subListBySegment(List<T> data, int segments) {
        List<List<T>> result = Lists.newArrayList();
        // 数据长度
        int size = data.size();
        // segments == 0 ，不需要分隔
        if (size > 0 && segments > 0) {
            // 每段数量
            int count = size / segments;
            for (int i = 0; i < segments; i++) {
                List<T> cutList = genCutList(data, count, size, segments, i);
                result.add(cutList);
            }
        } else {
            result.add(data);
        }
        return result;
    }

    /**
     * 分隔数组 根据每段数量分段
     *
     * segments 需要分隔的段数
     *
     * 若 data.size 整除 count 则分隔为 segments 段
     * 否则分隔为 segments + 1 段
     *
     * @param data  被分隔的数组
     * @param count 每段数量
     */
    public <T> List<List<T>> subListByCount(List<T> data, int count) {
        List<List<T>> result = new ArrayList<>();
        int size = data.size();
        if (size > 0 && count > 0) {
            int segments = size / count;
            segments = size % count == 0 ? segments : segments + 1;
            for (int i = 0; i < segments; i++) {
                List<T> cutList = genCutList(data, count, size, segments, i);
                result.add(cutList);
            }
        } else {
            result.add(data);
        }
        return result;
    }

    private <T> List<T> genCutList(List<T> data, int count, int size, int segments, int index) {
        List<T> cutList;
        // 最后一段数量可能不足count
        if (index == segments - 1) {
            cutList = data.subList(count * index, size);
        } else {
            cutList = data.subList(count * index, count * (index + 1));
        }
        return cutList;
    }

}
