package com.happy.rabbit.common;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author happy
 */
@Component
public class ThreadPoolCreate {

    public static Integer corePoolSize = 10;

    public static Integer maximumPoolSize = 20;

    public static Long keepAliveTime = 5L;

    static ArrayBlockingQueue queue = new ArrayBlockingQueue(10);


    public static ThreadPoolExecutor createBaseThreadPool() {
        ThreadPoolExecutor exe = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                queue, (r) -> {
            Thread thread = new Thread(r);
            thread.setName("min-thread:" + thread.getName());
            return thread;
        }, (r, executor) -> {
            System.out.println(r.toString() + " id discard!");
        }){
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                super.beforeExecute(t, r);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
            }
        };
        return exe;
    }
}
