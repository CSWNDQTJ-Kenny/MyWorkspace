package com.demo.caffeine.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CaffeineUtilTest {

    // Case 01
    /*public static void main(String[] args) throws Exception { // 测试Caffeine组件的基本操作
        Cache<String, String> cache_01 = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .build();
        cache_01.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache_01.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        System.out.println("【已超时获取缓存数据】Kenny = " + cache_01.getIfPresent("Kenny")); // 获取数据
    }*/

    // Case 02
    /*public static void main(String[] args) throws Exception { // 测试Caffeine组件的基本操作
        Cache<String, String> cache_01 = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .build();
        cache_01.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache_01.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        System.out.println("【已超时获取缓存数据】Kenny = " + cache_01.get("Kenny",
                (key) -> {
                    System.out.println("【失效处理】没有发现 KEY = " + key + " 的数据，要进行失效处理控制。");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "【EXPIRE】" + key; // 失效数据的返回
                })); // 获取数据
    }*/

    // Case 03
    public static void main(String[] args) throws Exception { // 测试Caffeine组件的基本操作
        LoadingCache<String, String> cache_01 = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .build(new CacheLoader<String, String>() {
                    @Override
                    public @Nullable String load(String key) throws Exception {
                        System.out.println("【CacheLoader】进行缓存数据的加载。。。");
                        TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                        return "【LoadingCache】" + key; // 数据加载的返回结果
                    }
                });

        cache_01.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache_01.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        cache_01.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
        for (Map.Entry<String, String> entry : cache_01.getAll(List.of("Kenny", "edu")).entrySet()) { // 重新加载缓存
            System.out.println("【已超时获取缓存数据, 数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
        }

    }

}