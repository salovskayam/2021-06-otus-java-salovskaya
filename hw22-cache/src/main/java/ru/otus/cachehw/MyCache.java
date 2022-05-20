package ru.otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(kvHwListener -> kvHwListener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        V removedValue = cache.remove(key);
        listeners.forEach(kvHwListener -> kvHwListener.notify(key, removedValue, "remove"));
    }

    @Override
    public V get(K key) {
        V returnedValue = cache.get(key);
        listeners.forEach(kvHwListener -> kvHwListener.notify(key, returnedValue, "get"));
        return returnedValue;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public void clear() {
        cache.clear();
    }


}
