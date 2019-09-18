package com.globalegrow.bigdata.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ：Chengjie
 * @date ：Created in 2019/8/4 22:40
 * @description：
 */
public class ThreadTest {

    public static void  main(String[] args) throws InterruptedException {

        ExecutorService e1 =Executors.newFixedThreadPool(3);

        ExecutorService e2 =Executors.newFixedThreadPool(3);

        ExecutorService e3 =Executors.newFixedThreadPool(3);

        e1.execute(new Runnable() {
            @Override
            public void run() {

                e2.execute(new Runnable() {
                    @Override
                    public void run() {

                         e3.execute(new Runnable() {
                             @Override
                             public void run() {
                                 for(int i=0;i<20;i++){
                                     try {
                                         System.out.println("【线程3】正在运行【"+i+"】");
                                         Thread.sleep(1000);
                                     } catch (InterruptedException e) {
                                     }
                                 }
                             }
                         });


                        for(int i=0;i<20;i++){
                            try {
                                System.out.println("【线程2】正在运行【"+i+"】");
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                });

                for(int i=0;i<20;i++){
                    try {
                        System.out.println("【线程1】正在运行【"+i+"】");
                        Thread.sleep(1000);
                        System.out.println("【线程1】的状态为【"+e1.isShutdown()+"】，【线程2】的状态为【"+e2.isShutdown()+"】，【线程3】的状态为【"+e3.isShutdown()+"】");

                    } catch (InterruptedException e) {
                    }
                }
            }
        });


        Thread.sleep(5000);
//        e1.awaitTermination(1,TimeUnit.SECONDS);
        e1.shutdownNow();

        System.out.println("【线程1】的状态为【"+e1.isShutdown()+"】，【线程2】的状态为【"+e2.isShutdown()+"】，【线程3】的状态为【"+e3.isShutdown()+"】");

    }


}
