package com.dfdyz.lcaddon.utils;

import java.util.concurrent.*;

public class ThreadUtils {
    private static final ExecutorService Pool = new ThreadPoolExecutor(512, 512, 0L,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2048),
            new ThreadPoolExecutor.AbortPolicy());

    public static void Start(Runnable e){
        Pool.submit(e);
    }
}
