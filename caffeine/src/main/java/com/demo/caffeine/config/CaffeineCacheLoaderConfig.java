package com.demo.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * author  : Kenny
 * time    :
 * description : Config to create Caffeine's CacheLoader
 */
@Configuration
public class CaffeineCacheLoaderConfig {

    @Bean("DefaultCacheLoader")
    public CacheLoader cacheLoaderDefault() {
        return new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                System.out.println("【DefaultCacheLoader】进行缓存数据的加载。。。");
                System.out.println("加载的 Key: " + key);
                TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                return "【DefaultCacheLoader】" + key;
            }
        };
    }

    /**
     * 标准的，可用于 Spring Cache 的 CacheLoader
     * @return
     */
    @Bean("ObjectCacheLoader")
    public CacheLoader<Object, Object> cacheLoaderObject() {
        return new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                System.out.println("【ObjectCacheLoader】进行缓存数据的加载。。。");
                TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                return key; // 数据加载的返回结果
            }
        };
    }

    /**
     * 使用 lambda 表达式，返回 CacheLoader
     * @return IntegerCacheLoader
     */
    @Bean("IntegerCacheLoader")
    public CacheLoader<Integer, Integer> cacheLoaderInteger() {
        return key -> {
            System.out.println("【IntegerCacheLoader】进行缓存数据的加载。。。");
            TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
            return key; // 数据加载的返回结果
        };
    }

    /**
     * 实现了更多 CacheLoader 接口中的方法
     * @return StringCacheLoader
     */
    @Bean("StringCacheLoader")
    public CacheLoader<String, String> cacheLoaderSting() {
        return new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                System.out.println("【StringCacheLoader】进行缓存数据的加载。。。");
                System.out.println("加载的 Key: " + key);
                TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                return "【StringCacheLoader】" + key;
            }

            @Override
            public CompletableFuture asyncLoad(String key, Executor executor) {
                System.out.println("【StringCacheLoader】进行缓存数据的 异步 加载。。。");
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println("异步加载的 Key: " + key);
                        return this.load(key);
                    } catch (RuntimeException var3) {
                        throw var3;
                    } catch (Exception var4) {
                        throw new CompletionException(var4);
                    }
                }, executor);
            }

            public String reload(String key, Object oldValue) throws Exception {
                System.out.println("【StringCacheLoader】进行缓存数据的 刷新。。。");
                System.out.println("刷新前的值：" + oldValue);
                System.out.println("刷新的 Key: " + key);
                return this.load(key);
            }

            /**
             * 只要配置了这个方法，必定会先于reload执行
             * */
            public CompletableFuture asyncReload(String key, Object oldValue, Executor executor) {
                System.out.println("【StringCacheLoader】进行缓存数据的 异步 刷新。。。");
                System.out.println("刷新前的值：" + oldValue);
                System.out.println("异步刷新的 Key: " + key);
                requireNonNull(key);
                requireNonNull(executor);
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println("start to reload");
                        return reload(key, oldValue);
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }, executor);
            }
        };
    }

}
