import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;

public class CustomThreadPool {
    private BlockingQueue<Runnable> workQueue;
    private final HashSet<Thread> workers = new HashSet<>();
    BackpressurePolicy backpressurePolicy;
    TaskListener taskListener;

    volatile boolean isShutdowned;

    public CustomThreadPool(int poolSize, int queueCapacity, boolean daemon,
                            BackpressurePolicy backpressurePolicy, TaskListener taskListener) {
        workQueue = new ArrayBlockingQueue<>(queueCapacity);
        this.backpressurePolicy = backpressurePolicy;
        this.taskListener = taskListener;

        for (int n = 0; n < poolSize; n++) {
            Thread thread = new Thread(this::runWorker);
            thread.setDaemon(daemon);
            workers.add(thread);
            thread.start();
        }
    }

    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();

        boolean added = workQueue.offer(command);
        if (added == false && backpressurePolicy == BackpressurePolicy.REJECT) {
            throw new RejectedExecutionException("");
        }
    }

    private void runWorker() {
        while (!isShutdowned) {
            try {
                Runnable task = workQueue.take();
                taskListener.startExecute();
                try {
                    task.run();
                } finally {
                    taskListener.endExecute();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void shutdown() {
        workers.forEach(Thread::interrupt);
        isShutdowned = true;
    }

}
