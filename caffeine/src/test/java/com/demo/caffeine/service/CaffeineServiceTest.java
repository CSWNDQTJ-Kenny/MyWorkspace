package com.demo.caffeine.service;

import com.demo.caffeine.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CaffeineServiceTest {


    @Autowired
    CaffeineService caffeineService;

    @Test
    void getEmployee() {
        Employee employee = caffeineService.getEmployee("1");
        System.out.println(employee.toString());
    }

    @Test
    void getEmployee_byCacheNames() {
        Employee employeeByCacheNames = caffeineService.getEmployee_byCacheNames("1");
        System.out.println(employeeByCacheNames.toString());
        System.out.println(employeeByCacheNames.toString());
        System.out.println(employeeByCacheNames.toString());
    }
}