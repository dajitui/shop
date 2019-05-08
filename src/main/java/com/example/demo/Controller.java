package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2019/5/7.
 *
 * @author yangsen
 */
@RestController
public class Controller {

    @Autowired
    Service service;

    @Autowired
    RocketMQProvider provider;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RocketMQConsumer consumer;

    CountDownLatch countDownLatch=new CountDownLatch(150);


    @RequestMapping("/shop")
    public void shop() throws InterruptedException {
        long time=System.nanoTime();
        for(int i=0;i<2000;i++){
            new Thread(new MyThread(String.valueOf(i),countDownLatch,service)).start();
        }
        countDownLatch.await();
        long time1=System.nanoTime();
        System.out.println("使用increment消耗时间"+(time1-time));
    }

    @RequestMapping("/shop1")
    public void shop1() throws InterruptedException {
        long time=System.nanoTime();
        for(int i=0;i<2000;i++){
            new Thread(new MyThread1(String.valueOf(i),countDownLatch,service)).start();
        }
        countDownLatch.await();
        long time1=System.nanoTime();
        System.out.println("使用分布锁消耗时间"+(time1-time));
    }

    @RequestMapping("/start")
    public void start() {
        System.out.println("start....");
        redisTemplate.opsForValue().set("1", 1500, 120, TimeUnit.SECONDS);
        consumer.defaultMQPushConsumer();
        while (true) {
            String name = null;//如果queue为null，那么5秒之后再去队列中取数据
            try {
                name = IServiceImpl.arrayBlockingQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (name != null) {
                System.out.println("...." + name);
                provider.defaultMQProducer(name);
            }
        }
    }

}
