package com.dfdyz.lcaddon.utils;

import java.util.concurrent.*;

public class ThreadUtils {
    private static final ExecutorService delayClientNotePool = new ThreadPoolExecutor(512, 512, 0L,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1024),
            new ThreadPoolExecutor.AbortPolicy());

    public static void Start(Runnable e){
        delayClientNotePool.submit(e);
    }

}
