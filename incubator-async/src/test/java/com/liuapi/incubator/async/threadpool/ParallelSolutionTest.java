package com.liuapi.incubator.async.threadpool;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ParallelSolutionTest {
    @Test
    void paralledStream() {
        // 设置并行流的线程数量
        ForkJoinPool myPool = new ForkJoinPool(10);
        List<MyTask> tasks = getTasks();
        long start = System.nanoTime();

        ForkJoinTask<List<Integer>> future = myPool.submit(
                () -> tasks.stream()
                        .parallel()
                        .map(MyTask::calculate)
                        .collect(Collectors.toList())
        );

        try {
            List<Integer> result = future.get();
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCompletableFuture() {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<MyTask> tasks = getTasks();
        long start = System.nanoTime();
        // 逐个提交到线程中处理并最终合并结果
        List<Integer> result = tasks.stream()
                .map(t -> CompletableFuture.supplyAsync(t::calculate, threadPool))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }


    List<MyTask> getTasks() {
        List<MyTask> tasks = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            tasks.add(new MyTask(1));
        }
        return tasks;
    }


    class MyTask {
        private final int duration;

        public MyTask(int duration) {
            this.duration = duration;
        }

        public int calculate() {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(duration * 1000);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
            return duration;
        }
    }
}
