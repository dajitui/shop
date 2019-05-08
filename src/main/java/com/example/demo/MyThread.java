package com.example.demo;

import java.util.concurrent.CountDownLatch;

/**
 * Created on 2019/5/7.
 *
 * @author yangsen
 */
public class MyThread implements Runnable {

    Service service;

    private String i;

    CountDownLatch latch;

    @Override
    public void run() {
        service.shop("dajitui" + i);
        latch.countDown();
    }

    public MyThread(String i, CountDownLatch latch,Service service) {
        this.service=service;
        this.latch = latch;
        this.i = i;
    }
}
