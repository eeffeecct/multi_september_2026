public class CustomThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        TaskListener listener = new TaskListener() {
            public void startExecute() {
                System.out.println(Thread.currentThread().getName() + " start");
            }
            public void endExecute() {
                System.out.println(Thread.currentThread().getName() + " end");
            }
        };
        CustomThreadPool pool = new CustomThreadPool(5, 10, true, BackpressurePolicy.DROP, listener);
        for (int i = 0; i < 5; i++) {
            int taskId = i;
            pool.execute(() -> System.out.println("task " + taskId + " on " + Thread.currentThread().getName()));
        }
        Thread.sleep(300);
    }
}