//package com.boot.util.common;
//
//public class CompletableFutureUtil {
//
//    private Instant start;
//
//    public static void main(String[] args) {
//        MainTest main = new MainTest();
//        main.start();
//    }
//
//    public void start() {
//        String req1 = "http://localhost:8080/testing";
//        String req2 = "http://127.0.0.1:8095/testing2";
//        ExecutorService exec = Executors.newCachedThreadPool();
//        start = Instant.now();
//        CompletableFuture<String> comp1 = CompletableFuture.supplyAsync(() -> doReq(req1), exec);
//        CompletableFuture<String> comp2 = CompletableFuture.supplyAsync(() -> doReq(req2), exec);
//        List<CompletableFuture<String>> completables = List.of(comp1, comp2);
//        System.out.println("Waiting completables");
//        List<String> r = getAllCompleted(completables, 3, TimeUnit.SECONDS);
//        Instant end = Instant.now();
//        System.out.println(" Took: " + DurationFormatUtils.formatDurationHMS(Duration.between(start, end).toMillis()));
//        System.out.println(r.size());
//        r.forEach(System.out::println);
//        exec.shutdown();
//    }
//
//    public String doReq(String request) {
//        if (request.contains("localhost")) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "response1";
//        }
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return "response2";
//    }
//
//    public <T> List<T> getAllCompleted(List<CompletableFuture<T>> futuresList, long timeout, TimeUnit unit) {
//        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[futuresList.size()]));
//        try {
//            allFuturesResult.get(timeout, unit);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return futuresList.stream().filter(// keep only the ones completed
//        future -> future.isDone() && !future.isCompletedExceptionally()).map(// get the value from the completed future
//        CompletableFuture::join).collect(// collect as a list
//        Collectors.<T>toList());
//    }
//}
//
//
//
//
//
// class CompletableFutureExample {
//
//
//
//    public static void main(String[] args) throws InterruptedException, ExecutionException {
//
//        ExecutorService executorService = Executors.newFixedThreadPool(5); // 创建一个固定大小的线程池
//
//
//
//        // 创建一个List来保存CompletableFuture对象
//
//        List<CompletableFuture<String>> futures = new ArrayList<>();
//
//
//
//        // 提交多个Callable任务到线程池，并包装成CompletableFuture
//
//        for (int i = 0; i < 10; i++) {
//
//            final int taskId = i;
//
//            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//
//                // 模拟耗时的计算
//
//                try {
//
//                    Thread.sleep((long) (Math.random() * 1000));
//
//                } catch (InterruptedException e) {
//
//                    Thread.currentThread().interrupt();
//
//                    throw new IllegalStateException(e);
//
//                }
//
//                return "Result of task " + taskId;
//
//            }, executorService); // 使用提供的ExecutorService执行
//
//            futures.add(future); // 将CompletableFuture保存到List中
//
//        }
//
//
//
//        // 使用CompletableFuture的allOf等待所有任务完成，然后遍历并获取结果
//
////        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();  这个会等待全部完成
//
//        for (CompletableFuture<String> future : futures) {
//            try {
//                // 设置5秒的超时
//                String result = future.get(5, TimeUnit.SECONDS);
//                System.out.println(result);
//            } catch (TimeoutException e) {
//                System.err.println("Task timed out!");
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//
//        // 关闭线程池
//
//        executorService.shutdown();
//
//    }
//
//}
