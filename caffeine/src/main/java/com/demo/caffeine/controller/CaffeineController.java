package com.demo.caffeine.controller;

import com.demo.caffeine.entity.Employee;
import com.demo.caffeine.service.CaffeineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * author  :
 * time    :
 * description :
 */
@RestController
public class CaffeineController {

    @Autowired
    CaffeineService caffeineService;

    @GetMapping("/employee/{id}")
    public String getEmployee(@PathVariable("id") String id) {
        Employee employee = caffeineService.getEmployee_byCacheNamesAndKey(id);
        return employee.toString();
    }

}
