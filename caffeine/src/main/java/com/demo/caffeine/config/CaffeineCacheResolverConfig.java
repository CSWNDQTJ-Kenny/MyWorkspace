package com.demo.caffeine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.NamedCacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author  :
 * time    :
 * description :
 */
@Configuration
public class CaffeineCacheResolverConfig {
    @Autowired
    private CacheManager cacheManager;

    @Bean("SimpleCacheResolver")
    public CacheResolver simpleCacheResolver(){
        SimpleCacheResolver resolver = new SimpleCacheResolver(cacheManager);
        return resolver;
    }

    @Bean("NamedCacheResolver")
    public CacheResolver namedCacheResolver(){
        NamedCacheResolver resolver = new NamedCacheResolver(cacheManager,"OutLimit");
        return resolver;
    }
}
