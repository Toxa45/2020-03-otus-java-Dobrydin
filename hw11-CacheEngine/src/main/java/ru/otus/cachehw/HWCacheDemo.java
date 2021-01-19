package ru.otus.cachehw;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HWCacheDemo {

  private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

  public static void main(String[] args) {
    new HWCacheDemo().demo();
  }

  @SneakyThrows
  private void demo() {
    HwCache<Integer, Integer> cache = new MyCache<>();

    // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
    HwListener<Integer, Integer> listener = new HwListener<Integer, Integer>() {
      @Override
      public void notify(Integer key, Integer value, String action) {
        logger.info("key:{}, value:{}, action: {}", key, value, action);
      }
    };
    cache.addListener(listener);
    cache.put(1, 1);

    logger.info("getValue:{}", cache.get(1));
    cache.remove(1);
    cache.removeListener(listener);
//    листенеры чистятся при gc если на них нет реальных ссылок
//    listener = null;
//    System.gc();
//    Thread.sleep(100);
  }
}
