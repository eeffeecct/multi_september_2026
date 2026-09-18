import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyPool {

    private Queue<Runnable> tasks;
    private Queue<Runnable> activeTasks;
    private Collection<Thread> workers;
    private int poolSize;
    private int maxTaskSize;
    private AtomicBoolean shutdown;
    private Runnable rejectPolicy;

    public MyPool(int poolSize, int tasksSize) {
        this.poolSize = poolSize;
        this.tasks = new ConcurrentLinkedQueue<>();
        this.activeTasks = new ConcurrentLinkedQueue<>();
        this.shutdown = new AtomicBoolean(false);
        this.rejectPolicy = REJECT;
        this.maxTaskSize = tasksSize;
        this.beforeRunInterceptor = new Runnable() {
            @Override
            public void run() {

            }
        };
        this.afterRunInterceptor = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    private Runnable beforeRunInterceptor;
    private Runnable afterRunInterceptor;

    public void start() {
        System.out.println("starting pool with: " + poolSize + " workers");
        this.workers = new LinkedList<>();

        (new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < poolSize; i++) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                           while(true){
                               try {
                                   Thread.sleep(10);
                               } catch (InterruptedException e) {
                                   throw new RuntimeException(e);
                               }

                               Runnable task = tasks.poll();
                               if (task != null) {
                                   activeTasks.add(task);
                                   beforeRunInterceptor.run();
                                   task.run();
                                   afterRunInterceptor.run();
                                   activeTasks.remove(task);
                               }
                           }
                        }
                    });
                    thread.setDaemon(true);
                    workers.add(thread);
                    thread.start();
                }

                while (!shutdown.get() || !activeTasks.isEmpty()) {
                    try {
                        Thread.sleep(100);
                        boolean isReady = !shutdown.get() || !activeTasks.isEmpty();
                        System.out.println(isReady + " " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        })).start();
    }

    public void setInterceptorBeforeRun(Runnable beforeRunInterceptor) {
        this.beforeRunInterceptor = beforeRunInterceptor;
    }

    public void setAfterRunInterceptor(Runnable afterRunInterceptor) {
        this.afterRunInterceptor = afterRunInterceptor;
    }

    public void shutdown() {
        shutdown.set(true);
    }

    public void setRejectPolicy(Runnable rejectPolicy) {
        this.rejectPolicy = rejectPolicy;
    }

    public void submit(Runnable task) {
        if (tasks.size() >= maxTaskSize) {
            this.rejectPolicy.run();
        } else {
            tasks.add(task);
        }
    }


    public static Runnable REJECT = new Runnable() {
        @Override
        public void run() {
            throw new RuntimeException("Task size is already exceed max size");
        }
    };

    public static Runnable DROP = new Runnable() {
        @Override
        public void run() {
            System.out.println("Dropping task due size limit");
        }
    };

}
