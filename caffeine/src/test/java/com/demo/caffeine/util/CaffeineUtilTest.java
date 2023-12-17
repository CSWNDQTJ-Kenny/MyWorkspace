package com.demo.caffeine.util;

import com.github.benmanes.caffeine.cache.*;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * author  :
 * time    :
 * description :
 */
class CaffeineUtilTest {

    /**
     * Case 01
     *
     * @throws InterruptedException
     */
    @Test
    void sync_case_01() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(3L, TimeUnit.SECONDS)
                .build();
        cache.put("kenny", "cswndqtj@sina.com");
        System.out.println("[Get un-expiry cache data] kenny = " + cache.getIfPresent("kenny"));
        TimeUnit.SECONDS.sleep(5);
        System.out.println("[Get expiry cache data] Kenny = " + cache.getIfPresent("kenny"));
    }

    @Test
    void sync_case_02() throws Exception {
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(3L, TimeUnit.SECONDS)
                .build();
        cache.put("kenny", "cswndqtj@sina.com");
        System.out.println("[Get un-expiry cache data] kenny = " + cache.getIfPresent("kenny"));
        TimeUnit.SECONDS.sleep(5);
        System.out.println("[Get expiry cache data] kenny = " + cache.get("kenny",
                (key) -> {
                    System.out.println("[Handle Expiry] can't get value of KEY = " + key + ", need a expiry handler");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "[EXPIRY] " + key;
                }));
    }

    /**
     * Test auto refresh expired data
     * @throws InterruptedException
     */
    @Test
    void sync_case_03() throws InterruptedException {
        LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterWrite(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .scheduler(Scheduler.systemScheduler())// 开启定期清空过期数据
                .build(new CacheLoader<String, String>() {
                    @Override
                    public @Nullable String load(String key) throws Exception {
                        System.out.println("【CacheLoader】Loading Cache Data...");
                        TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                        return "【LoadingCache】" + key; // 数据加载的返回结果
                    }
                });

        cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【Cache Data】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
        System.out.println("【Time Out Cache Data, Loading...】key = Kenny, value = " + cache.getIfPresent("Kenny")); // 重新加载数据
        System.out.println("【New Cache Data】edu = " + cache.getIfPresent("edu")); // 获取新添加数据
        //System.out.println("[Expiry cache data] kenny = " + cache.get("Kenny",
        //        (key) -> {
        //            System.out.println("[Handle Expiry] can't get value of KEY = " + key + ", need a expiry handler");
        //            try {
        //                TimeUnit.SECONDS.sleep(2);
        //            } catch (InterruptedException e) {
        //                throw new RuntimeException(e);
        //            }
        //            return "[Re-Loading EXPIRY Data] " + key;
        //        }));
        //for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu", "ooxx")).entrySet()) { // 重新加载缓存
        //    System.out.println("【Time Out Cache Data, Loading...】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
        //}
    }

    /**
     * Test auto refresh expired data
     * @throws InterruptedException
     */
    @Test
    void sync_case_04() throws InterruptedException {
        LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
                .maximumSize(100) // 设置缓存之中保存的最大数据量
                .expireAfterWrite(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
                .scheduler(Scheduler.systemScheduler())// 开启定期清空过期数据
                .build(new CacheLoader<String, String>() {
                    @Override
                    public @Nullable String load(String key) throws Exception {
                        System.out.println("【CacheLoader】Loading Cache Data...");
                        TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                        return "【LoadingCache】" + key; // 数据加载的返回结果
                    }
                });

        cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
        System.out.println("【Cache Data】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
        TimeUnit.SECONDS.sleep(5); // 5秒后超时
        cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
        System.out.println("【Time Out Cache Data, Loading...】key = Kenny, value = " + cache.getIfPresent("Kenny")); // 重新加载数据
        System.out.println("【New Cache Data】edu = " + cache.getIfPresent("edu")); // 获取新添加数据
        //System.out.println("[Expiry cache data] kenny = " + cache.get("Kenny",
        //        (key) -> {
        //            System.out.println("[Handle Expiry] can't get value of KEY = " + key + ", need a expiry handler");
        //            try {
        //                TimeUnit.SECONDS.sleep(2);
        //            } catch (InterruptedException e) {
        //                throw new RuntimeException(e);
        //            }
        //            return "[Re-Loading EXPIRY Data] " + key;
        //        }));
        for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu", "t0pnl")).entrySet()) { // 重新加载缓存
            System.out.println("【Time Out Cache Data, Loading...】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
        }
    }

    @Test
    void array_list_test() {
        ArrayList<String> strList = new ArrayList<>();
        strList.add("1");
        strList.add("1");
        System.out.println(strList);
    }
    @Test
    void encode_utf8() {
        System.out.println("\u4f60\u597d"); // 你好
    }

}