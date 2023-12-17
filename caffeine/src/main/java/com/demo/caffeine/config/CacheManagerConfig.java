package com.demo.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author  :
 * time    :
 * description :
 */
@Configuration
public class CacheManagerConfig {

//    @Value("${caffeine.spec}")
//    private String caffeineSpec;

    //@Autowired
    //private CacheLoader cacheLoader;

    @Autowired
    @Qualifier("SimpleStringCacheLoader")
    private CacheLoader simpleStringCacheLoader;

    @Autowired
    @Qualifier("T0PnLCaffeineCache")
    private Caffeine<Object, Object> t0PnLCaffeineCache;

    @Primary
    @Bean("T0PnLCacheManager")
    public CacheManager t0PnLCacheManager() {
        System.out.println("Now is T0PnLCacheManager");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(t0PnLCaffeineCache);
        //cacheManager.setCacheLoader(simpleStringCacheLoader);
        cacheManager.setCacheNames(List.of("T0PnLCache"));
        return cacheManager;
    }


    @Bean("CacheManagerWithCacheLoading")
    public CacheManager cacheManagerWithCacheLoading(){
        System.out.println("Now is cacheManagerWithCacheLoading");
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
//                .refreshAfterWrite(5,TimeUnit.SECONDS)
                .expireAfterWrite(50, TimeUnit.SECONDS);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheLoader(cacheLoader);
        cacheManager.setCacheNames(getNames());
        return cacheManager;
    }



    @Bean(name = "CacheManagerWithCaffeine")
//    @Primary
    public CacheManager cacheManagerWithCaffeine(){
        System.out.println("Now is cacheManagerWithCaffeine");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                //cache的初始容量值
                .initialCapacity(100)
                //maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight不可以同时使用，
                .maximumSize(1000);
        //控制最大权重
//                .maximumWeight(100);
//                .expireAfter();
        //使用refreshAfterWrite必须要设置cacheLoader
//                .refreshAfterWrite(5,TimeUnit.SECONDS);
        cacheManager.setCaffeine(caffeine);
        //cacheManager.setCacheLoader(cacheLoader);
        cacheManager.setCacheNames(getNames());
//        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

//    @Bean(name = "caffeineSpec")
//    public CacheManager cacheManagerWithCaffeineFromSpec(){
//        CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
//        Caffeine caffeine = Caffeine.from(spec);
//        //此方法等同于上面from(spec)
////        Caffeine caffeine = Caffeine.from(caffeineSpec);
//
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheNames(getNames());
//        return cacheManager;
//    }

    /**
     * 可以实现自己生成key的策略
     * */
//    @Bean(name = "SimpleKeyGenerator")
//    public KeyGenerator keyGenerator(){
//        return new SimpleKeyGenerator();
//    }

    private static List<String> getNames(){
        List<String> names = new ArrayList<>(2);
        names.add("outLimit");
        names.add("notOutLimit");
        return names;
    }

}
