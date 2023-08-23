package com.demo.caffeine.util;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CaffeineUtilTest {

    // Case 01
    @Test
    void sync_case_01() throws Exception { // 测试Caffeine组件的基本操作
        Cache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .build();
        cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        System.out.println("【已超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
    }

    // Case 02
    @Test
    void sync_case_02() throws Exception { // 测试Caffeine组件的基本操作
        Cache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .build();
        cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        System.out.println("【已超时获取缓存数据】Kenny = " + cache.get("Kenny",
                (key) -> {
                    System.out.println("【失效处理】没有发现 KEY = " + key + " 的数据，要进行失效处理控制。");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "【EXPIRE】" + key; // 失效数据的返回
                })); // 获取数据
    }

    // Case 03
    @Test
    void sync_case_03() throws Exception { // 测试Caffeine组件的基本操作
        LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
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

        cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
        for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).entrySet()) { // 重新加载缓存
            System.out.println("【已超时获取缓存数据, 数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
        }

    }

    // Case 04
    @Test
    void sync_case_04() throws Exception { // 测试Caffeine组件的基本操作
        LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .build(new CacheLoader<String, String>() {
                    @Override
                    public @Nullable String load(String key) throws Exception {
                        System.out.println("【CacheLoader】进行缓存数据的加载,当前的KEY = " + key);
                        TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                        return "【LoadingCache】" + key; // 数据加载的返回结果
                    }
                });

        cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
        for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).entrySet()) { // 重新加载缓存
            System.out.println("【数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
        }
        System.out.println("【已超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
    }

    // Case 05
    /**
     * 相比之前的 get() 方法同步加载数据，此时使用专属的功能接口完成了数据加载，更加标准化
     * @throws InterruptedException
     */
    @Test
    void sync_case_05() throws InterruptedException {
        LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .build((key) -> {
                        System.out.println("【CacheLoader】进行缓存数据的加载,当前的KEY = " + key);
                        TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                        return "【LoadingCache】" + key; // 数据加载的返回结果

                }); //构建 Cache接口实例

        cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
        for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).entrySet()) { // 重新加载缓存
            System.out.println("【数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
        }
        System.out.println("【已超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
    }


    // Case 06
    @Test
    void async_case_01() throws ExecutionException, InterruptedException {
        AsyncLoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .buildAsync((key, executor) ->
                    CompletableFuture.supplyAsync(() -> {
                        System.out.println("【AsyncCacheLoader】进行缓存数据的加载,当前的KEY = " + key);
                        try {
                            TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return "【AsyncCacheLoader】" + key; // 数据加载的返回结果

                    })
                ); //构建 Cache接口实例
        // 此时是一个异步的缓存管理，所以在这个时候进行数据追加处理时，也要采用异步的方式
        cache.put("Kenny", CompletableFuture.completedFuture("cswndqtj@sina.com")); // 追加缓存项
        System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny").get()); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        cache.put("edu", CompletableFuture.completedFuture("edu.heima.com")); // 缓存失效之后 继续追加新的数据
        for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).get().entrySet()) { // 重新加载缓存
            System.out.println("【数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
        }
    }

}