package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;


public class MyCache<K, V> implements HwCache<K, V> {

  private final static String PUT = "put";
  private final static String GET = "get";
  private final static String REMOVE = "remove";
  private final Map<Object, V> cache = new WeakHashMap<>();
  private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();
  //Надо реализовать эти методы

  private Object convertKey(K key) {
    if (key instanceof Number) {
      return key.toString();
    } else {
      return key;
    }
  }

  @Override
  public void put(K key, V value) {
    cache.put(convertKey(key), value);
    notify(key, value, PUT);
  }

  @Override
  public void remove(K key) {
    notify(key, cache.remove(convertKey(key)), REMOVE);
  }

  @Override
  public V get(K key) {
    V elCache = cache.get(convertKey(key));
    notify(key, elCache, GET);
    return elCache;
  }

  @Override
  public void addListener(HwListener<K, V> listener) {
    listeners.add(new WeakReference(listener));
  }

  @Override
  public void removeListener(HwListener<K, V> listener) {
    listeners.stream().filter(el -> el.get() != null && el.get().equals(listener)).findFirst()
        .ifPresent(el -> listeners.remove(el));
  }


  private void notify(K key, V value, String action) {
    int countListeners = listeners.size();
    for (int indexListener = 0; indexListener < countListeners; indexListener++) {
      try {
        WeakReference<HwListener<K, V>> listener = listeners.get(indexListener);
        HwListener<K, V> kvHwListener = listener.get();
        if (kvHwListener != null) {
          kvHwListener.notify(key, value, action);
        } else {
          listeners.remove(listener);
          countListeners--;
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
