package com.demo.caffeine.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class T0PnLReconServiceTest {

    @Autowired
    private T0PnLReconService t0PnLReconService;

    @Test
    void getT0PnL_byCacheNames() {
    }

    @Test
    void getT0PnL_byCacheNamesAndKey() throws InterruptedException {
        Date fetchDate = new Date();
        //t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC");
        //t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC");
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "EMEA"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "EMEA"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "NAM"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "NAM"));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "NAM"));
        TimeUnit.SECONDS.sleep(6);
        //t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC");
        //t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC");
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "APAC"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "EMEA"));
        System.out.println(t0PnLReconService.getT0PnL_byCacheNamesAndKey(fetchDate, "NAM"));
    }

    @Test
    void getByRegion() throws InterruptedException {
        System.out.println(t0PnLReconService.getByRegion("APAC"));
        TimeUnit.SECONDS.sleep(1);
        System.out.println(t0PnLReconService.getByRegion("EMEA"));
        TimeUnit.SECONDS.sleep(1);
        System.out.println(t0PnLReconService.getByRegion("NAM"));
        TimeUnit.SECONDS.sleep(6);
        System.out.println(t0PnLReconService.getByRegion("EMEA"));
        System.out.println(t0PnLReconService.getByRegion("APAC"));
        System.out.println(t0PnLReconService.getByRegion("NAM"));
    }
}