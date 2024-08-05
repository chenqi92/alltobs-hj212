package com.alltobs.hj212.model.verify;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 解决无法对MAP进行验证定义问题
 *
 * @author ChenQi
 */
public class T212Map<K, V>
        implements Map<K, V>, Serializable {

    private Map<K, V> m;

    public T212Map(Map<K, V> m) {
        this.m = m;
    }

    public static T212DataLevelMap createDataLevel(Map<String, String> map) {
        return new T212DataLevelMap(map);
    }

    public static T212CpDataLevelMap createCpDataLevel(Map<String, Object> map) {
        return new T212CpDataLevelMap(map);
    }

    @Override
    public int size() {
        return m.size();
    }

    @Override
    public boolean isEmpty() {
        return m.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return m.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return m.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return m.get(key);
    }

    @Override
    public V put(K key, V value) {
        return m.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return m.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        this.m.putAll(m);
    }

    @Override
    public void clear() {
        m.clear();
    }

    @Override
    public Set<K> keySet() {
        return m.keySet();
    }

    @Override
    public Collection<V> values() {
        return m.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return m.entrySet();
    }

}
