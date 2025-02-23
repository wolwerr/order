package org.test.order.domain.gateway.cache;

public interface CacheInterface {
    void cacheData(String key, Object data);
    Object getDataFromCache(String key);
}