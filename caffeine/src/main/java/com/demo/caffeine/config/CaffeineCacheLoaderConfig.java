package com.demo.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

/**
 * author  :
 * time    :
 * description :
 */
@Configuration
public class CaffeineCacheLoaderConfig {

    @Bean("StringCacheLoader")
    public CacheLoader<String, String> cacheLoader() {
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
