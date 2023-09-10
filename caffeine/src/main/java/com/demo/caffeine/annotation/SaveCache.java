package com.demo.caffeine.annotation;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

@Caching(put = {
        @CachePut(cacheNames = "emps", key = "#employee.id"),
        @CachePut(cacheNames = "emps", key = "#employee.name"),
        @CachePut(cacheNames = "emps", key = "#employee.age")
})
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SaveCache {
}
