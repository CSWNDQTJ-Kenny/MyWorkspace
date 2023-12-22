package com.demo.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * author  :
 * time    :
 * description :
 */
@Configuration
public class CacheManagerConfig {

    @Resource(name = "SizeCache")
    private Caffeine sizecCache;
    @Resource(name = "RemovalListenerCache")
    private Caffeine removalListenerCache;

    @Resource(name = "DefaultCacheLoader")
    private CacheLoader defaultCacheLoader;
    @Resource(name = "ObjectCacheLoader")
    private CacheLoader<Object, Object> objectCacheLoader;

    //@Value("${caffeine.spec}")
    //private String caffeineSpec;


    @Bean(name = "SizeCacheManager")
    public CacheManager cacheManagerWithSizeCache() {
        System.out.println("Config cacheManager with SizeCache");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("maxSize"));
        cacheManager.setCaffeine(sizecCache);
        cacheManager.setCacheLoader(objectCacheLoader);
        cacheManager.setAllowNullValues(true);
        return cacheManager;
    }


    @Bean(name = "RemovalListenerCacheManager")
    public CacheManager cacheManagerWithRemovalListenerCache() {
        System.out.println("Config cacheManager with RemovalListenerCache");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(removalListenerCache);
        cacheManager.setCacheLoader(defaultCacheLoader);
        cacheManager.setCacheNames(getNames());
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }


//    @Bean(name = "CaffeineSpec")
//    public CacheManager cacheManagerWithCaffeineSpec(){
//        CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
//        Caffeine caffeine = Caffeine.from(spec);
//
//        //Caffeine caffeine = Caffeine.from(caffeineSpec); //此方法等同于上面from(spec)
//
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheNames(getNames());
//        return cacheManager;
//    }


    private static List<String> getNames() {
        List<String> names = new ArrayList<>(2);
        names.add("xxoo");
        names.add("ooxx");
        return names;
    }

}
