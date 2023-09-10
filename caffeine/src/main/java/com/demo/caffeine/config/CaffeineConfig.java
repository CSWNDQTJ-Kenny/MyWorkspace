package com.demo.caffeine.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * author  :
 * time    :
 * description :
 */
@Configuration
public class CaffeineConfig {

    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                System.out.println("***" + target);
                System.out.println("***" + method);
                System.out.println("***" + params);
                return method.getName() + "[" + Arrays.asList(params).toString() + "]";
            }
        };
    }

}
