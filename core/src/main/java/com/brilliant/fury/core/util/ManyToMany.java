package com.brilliant.fury.core.util;

import com.google.common.base.MoreObjects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.util.Set;

/**
 * 双向 MAP : key 和 value 可以相互查找.
 *
 * @author by fury.
 * version 2020/1/16.
 */
public class ManyToMany<K, V> {

    private final SetMultimap<K, V> keysToValues = HashMultimap.create();
    private final SetMultimap<V, K> valuesToKeys = HashMultimap.create();

    /**
     * 双写
     */
    public boolean put(K key, V value) {
        return keysToValues.put(key, value) && valuesToKeys.put(value, key);
    }

    public boolean putAll(K key, Iterable<? extends V> values) {
        boolean changed = false;
        for (V value : values) {
            changed |= put(key, value);
        }
        return changed;
    }

    /**
     * 通过 key 获取 values
     */
    public Set<V> getValues(K key) {
        return keysToValues.get(key);
    }

    /**
     * 通过 value 获取 keys
     */
    public Set<K> getKeys(V value) {
        return valuesToKeys.get(value);
    }

    /**
     * 获取 key set
     */
    public Set<K> getKeySet() {
        return keysToValues.keySet();
    }

    /**
     * 获取 value set
     */
    public Set<V> getValueSet() {
        return valuesToKeys.keySet();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("[keysToValues]:", keysToValues.toString())
            .add("[valuesToKeys]:", valuesToKeys.toString())
            .toString();
    }
}
