package com.demo.caffeine.service;

import com.demo.caffeine.entity.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * author  :
 * time    :
 * description :
 */


@Service
public class CaffeineService {

    public Employee getEmployee(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", key = "#id") // #id means id is the key to get value
    public Employee getEmployee_byCacheNamesAndKey(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = {"emps", "caffeine"}) // also can name multiple caches
    public Employee getEmployee_byCacheNames(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", key = "#root.methodName + '[' + #id + ']'")
    public Employee getEmployee_byKey(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", keyGenerator = "myKeyGenerator") // This myKeyGenerator is customed by ourselves
    public Employee getEmployee_byCacheNamesAndKeyGenerator(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", condition = "#id > 1") // When id > 1, then save the result into cache
    public Employee getEmployee_byCacheNamesAndCondition(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", unless = "#id > 1") // When id <= 1, then save the result into cache
    public Employee getEmployee_byCacheNamesAndUnless(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

}
