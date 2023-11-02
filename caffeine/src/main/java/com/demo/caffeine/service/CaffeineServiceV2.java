package com.demo.caffeine.service;

import com.demo.caffeine.annotation.SaveCache;
import com.demo.caffeine.entity.Employee;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * author  :
 * time    :
 * description :
 */

/**
 * Class Annotation:  @Cacheable、@CachePut、@CacheEvict、@Caching、@CacheConfig
 * These annotation can use upon a class, and all public method in that class will be impacted
 */

/**
 * @CacheConfig 该注解是类级别的，可以在类上配置cacheNames、keyGenerator、cacheManager、cacheResolver等参数
 */


@Service
@CacheConfig(cacheNames = "caffeine")
public class CaffeineServiceV2 {

    @Cacheable
    public Employee getEmployee(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @CachePut
    public Employee updateEmployee(String id) {
        System.out.println("Update " + id);
        return new Employee("1", "Kenny", 30);
    }

    @CacheEvict
    public void deleteEmployee(String id) {
        System.out.println("Delete " + id);
        System.out.println("*** Already deleted ID: " + id);
    }

    @SaveCache
    public Employee saveEmployee(Employee employee) {
        System.out.println("Save " + employee.toString());
        return new Employee("1", "Kenny", 30);
    }
    

}
