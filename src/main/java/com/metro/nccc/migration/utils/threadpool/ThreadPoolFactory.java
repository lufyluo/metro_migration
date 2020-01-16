package com.metro.nccc.migration.utils.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-12-31 上午10:52
 */
@Component
public class ThreadPoolFactory {
    @Bean("fixedPool")
    public ExecutorService fixedPool() {
        int coreNum = Runtime.getRuntime().availableProcessors() * 2;
        return new ThreadPoolExecutor(coreNum, coreNum,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new MyIgnorePolicy());
    }

    public static class MyIgnorePolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.err.println(r.toString() + " rejected");
            System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }

}
