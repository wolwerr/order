package org.test.order.infra.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.test.order.domain.gateway.cache.CacheInterface;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCache implements CacheInterface {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void cacheData(String key, Object data) {
        redisTemplate.opsForValue().set(key, data, 10, TimeUnit.SECONDS);
    }

    @Override
    public Object getDataFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}