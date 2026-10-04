package com.example;

import jdk.internal.vm.annotation.Contended;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Padding {
    private static class Data {
//        int p8;
//        long q1, q2, q3, q4, q5, q6, q7;
        @Contended
        volatile long value0;
//        long p1, p2, p3, p4, p5, p6, p7;
        @Contended
        volatile int value1;
    }

    static class SharedCounters {
        Data data;
//        int f0;
        @Contended
        volatile long c3;
//        long p1, p2, p3, p4, p5, p6, p7;
        @Contended
        volatile int c1;
//        int f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15;
        @Contended
        volatile short c0;
//        short s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16,
//                s17, s18, s19, s20, s21, s22, s23, s24, s25, s26, s27, s28, s29, s30, s31;
        @Contended
        volatile boolean c2;
//        byte b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16,
//                b17, b18, b19, b20, b21, b22, b23, b24, b25, b26, b27, b28, b29, b30, b31, b32,
//                b33, b34, b35, b36, b37, b38, b39, b40, b41, b42, b43, b44, b45, b46, b47, b48,
//                b49, b50, b51, b52, b53, b54, b55, b56, b57, b58, b59, b60, b61, b62, b63;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(ClassLayout.parseClass(Data.class).toPrintable());
        System.out.println(ClassLayout.parseClass(SharedCounters.class).toPrintable());

        long start = System.currentTimeMillis();
        SharedCounters sc = new SharedCounters();
        sc.data = new Data();
        CountDownLatch countDownLatch = new CountDownLatch(6);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 100_000_000; j++) {
                    sc.data.value0++;
                }
                countDownLatch.countDown();
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 100_000_000; j++) {
                    sc.data.value1++;
                }
                countDownLatch.countDown();
            }
        });
        t2.start();
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 100_000_000; j++) {
                    sc.c0++;
                }
                countDownLatch.countDown();
            }
        });
        t3.start();
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 100_000_000; j++) {
                    sc.c2 = !sc.c2;
                }
                countDownLatch.countDown();
            }
        });
        t4.start();
        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 100_000_000; j++) {
                    sc.c3++;
                }
                countDownLatch.countDown();
            }
        });
        t5.start();
        Thread t6 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 100_000_000; j++) {
                    sc.c1++;
                }
                countDownLatch.countDown();
            }
        });
        t6.start();

        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("time elapsed: " + (end - start) / 1000f);
    }

}
