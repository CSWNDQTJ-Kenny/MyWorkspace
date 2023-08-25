package com.demo.caffeine.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * author  :
 * time    :
 * description :
 */
public class CaffeineTest {

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


}
