package com.example.demo;

import java.util.concurrent.CountDownLatch;

/**
 * Created on 2019/5/7.
 *
 * @author yangsen
 */
public class MyThread1 implements Runnable {

    Service service;

    private String i;

    CountDownLatch latch;

    @Override
    public void run() {
        service.shop1("dajitui" + i);
        latch.countDown();
    }

    public MyThread1(String i, CountDownLatch latch, Service service) {
        this.service=service;
        this.latch = latch;
        this.i = i;
    }
}
