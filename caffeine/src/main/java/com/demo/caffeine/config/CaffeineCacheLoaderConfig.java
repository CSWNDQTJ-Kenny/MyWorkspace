package com.demo.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * author  :
 * time    :
 * description :
 */
@Configuration
public class CaffeineCacheLoaderConfig {

    //@Autowired
    //@Qualifier("T0PnLCacheManager")
    //private CacheManager t0PnLCacheManager;

    //@Autowired
    //@Qualifier("T0PnLCache")
    //private Cache<String, String> t0PnLCache;

    @Bean("SimpleStringCacheLoader")
    public CacheLoader<String, String> simpleStringCacheLoader() {
        return new CacheLoader<String, String>() {
            @Override
            public @Nullable String load(String key) throws Exception {
                System.out.println("【SimpleStringCacheLoader】进行缓存数据的加载。。。");
                TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
                //org.springframework.cache.Cache cacheInT0PnLCacheManager = t0PnLCacheManager.getCache("T0PnLCache");
                //cacheInT0PnLCacheManager.put("APAC", "【LoadingCache】- APAC");
                //cacheInT0PnLCacheManager.put("EMEA", "【LoadingCache】- EMEA");
                //cacheInT0PnLCacheManager.put("NAM", "【LoadingCache】- NAM");
                //return cacheInT0PnLCacheManager.get(key).toString();
                //t0PnLCache.put("APAC", "【LoadingCache】- APAC");
                //t0PnLCache.put("EMEA", "【LoadingCache】- EMEA");
                //t0PnLCache.put("NAM", "【LoadingCache】- NAM");
                //return t0PnLCache.getIfPresent(key);
                return "【LoadingCache】" + key; // 数据加载的返回结果
            }
        };
    }

    @Bean("FullMethodStringCacheLoader")
    public CacheLoader<String, String> fullMethodStringCacheLoader() {
        return new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                System.out.println(System.currentTimeMillis() + " This is load key = " + key);
                return key;
//                throw new Exception();
//                if (String.valueOf(key).equals("kun")){
//                    return "kun kun ni hao";
//                }
//                return key + "ni hao";
            }

//            @Override
            public String reload(String key, Object oldValue) throws Exception {
                System.out.println(System.currentTimeMillis() + " oldValue = " + oldValue);
                return this.load(key);
//                return oldValue+" a";
            }

            /**
             * 只要配置了这个方法，必定会先于reload执行
             * */
//            @Override
            public CompletableFuture asyncReload(String key, Object oldValue, Executor executor) {
                System.out.println("asyncReload key = " + key + ", oldValue =" + oldValue);

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

            @Override
            public CompletableFuture asyncLoad(String key, Executor executor) {
                System.out.println(System.currentTimeMillis() + " asyncLoadkey = " + key);
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        return this.load(key);
                    } catch (RuntimeException var3) {
                        throw var3;
                    } catch (Exception var4) {
                        throw new CompletionException(var4);
                    }
                }, executor);
            }

        };
    }

}
