package com.demo.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author  : Kenny
 * time    :
 * description : Config to create a simple caffeine in spring cache way
 */
@Configuration
public class CaffeineConfig {

    @Primary
    @Bean
    public CacheManager primaryCaffeineCacheManager(){
        System.out.println("Config to create primaryCaffeineCacheManager");
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .refreshAfterWrite(5,TimeUnit.SECONDS); //使用 refreshAfterWrite 时建议设置cacheLoader


        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheLoader(primaryCacheLoader());
        cacheManager.setCacheNames(getPrimaryNames());
        return cacheManager;
    }

    public CacheLoader primaryCacheLoader() {
        return new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                System.out.println("【PrimaryCacheLoader】进行缓存数据的加载。。。");
                System.out.println("加载的 Key: " + key);
                TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                return "【PrimaryCacheLoader】" + key;
            }
        };
    }

    private static List<String> getPrimaryNames(){
        List<String> names = new ArrayList<>(1);
        names.add("primary");
        return names;
    }



    @Bean(name = "simpleCaffeine")
    public CacheManager simpleCaffeineCacheManager(){
        System.out.println("Config to create simpleCaffeineCacheManager");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .initialCapacity(100) //cache的初始容量值
                //.maximumWeight(100); //控制最大权重
                .maximumSize(1000); //maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight不可以同时使用，

        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheLoader(simpleCacheLoader());
        cacheManager.setCacheNames(getSimpleNames());
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    public CacheLoader<Object, Object> simpleCacheLoader() {
        return new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                System.out.println("【SimpleCacheLoader】进行缓存数据的加载。。。");
                System.out.println("加载的 Key: " + key);
                TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                return "【SimpleCacheLoader】" + key;
            }
        };
    }

    private static List<String> getSimpleNames(){
        List<String> names = new ArrayList<>(2);
        names.add("user");
        names.add("employee");
        return names;
    }

}
