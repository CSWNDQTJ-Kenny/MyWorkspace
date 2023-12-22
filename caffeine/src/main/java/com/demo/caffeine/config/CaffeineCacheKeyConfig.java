package com.demo.caffeine.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
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
public class CaffeineCacheKeyConfig {

    /**
     * 可以实现自己生成key的策略
     * */
    @Bean(name = "SimpleKeyGenerator")
    public KeyGenerator simplekeyGenerator(){
        return new SimpleKeyGenerator();
    }

    @Bean("myKeyGenerator")
    public KeyGenerator mykeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                System.out.println("***" + target);
                System.out.println("***" + method);
                System.out.println("***" + params);
                return method.getName() + "[ " + Arrays.asList(params).toString() + " ]";
            }
        };
    }

}
