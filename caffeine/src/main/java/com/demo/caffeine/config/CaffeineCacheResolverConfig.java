package com.demo.caffeine.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.NamedCacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * author  :
 * time    :
 * description :
 */
@Configuration
public class CaffeineCacheResolverConfig {
    @Resource(name = "RemovalListenerCacheManager")
    private CacheManager removalListenerCacheManager;

    @Bean("SimpleCacheResolver")
    public CacheResolver simpleCacheResolver(){
        SimpleCacheResolver resolver = new SimpleCacheResolver(removalListenerCacheManager);
        return resolver;
    }

    @Bean("NamedCacheResolver")
    public CacheResolver namedCacheResolver(){
        NamedCacheResolver resolver = new NamedCacheResolver(removalListenerCacheManager,"trader");
        return resolver;
    }
}
