import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyNicePool {

    private Queue<Runnable> tasks;
    private Queue<Runnable> runningTasks;
    private volatile boolean shutdown;
    private int maxTaskSize;
    private int workersSize;
    private Runnable beforeRun;
    private Runnable afterRun;



    public void setRejectPolicy(Runnable rejectPolicy) {
        this.rejectPolicy = rejectPolicy;
    }

    private Runnable rejectPolicy;

    public MyNicePool(int maxTaskSize, int workersSize) {
        this.tasks = new ConcurrentLinkedQueue<>();
        this.runningTasks = new ConcurrentLinkedQueue<>();
        this.maxTaskSize = maxTaskSize;
        this.workersSize = workersSize;
        this.rejectPolicy = REJECT;
        this.beforeRun = new Runnable() {
            @Override
            public void run() {

            }
        };
        this.afterRun = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    public void setBeforeRun(Runnable beforeRun) {
        this.beforeRun = beforeRun;
    }

    public void setAfterRun(Runnable afterRun) {
        this.afterRun = afterRun;
    }

    public void start() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < workersSize; i++) {
                    Thread tempThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!shutdown) {
                                Runnable task = tasks.poll();
                                if (task != null) {
                                    runningTasks.add(task);
                                    beforeRun.run();
                                    task.run();
                                    afterRun.run();
                                    runningTasks.remove(task);
                                }
                            }
                        }
                    });
                    tempThread.setDaemon(true);
                    tempThread.start();
                }
                while (!shutdown || !runningTasks.isEmpty()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        })).start();
    }

    public void submit(Runnable task) {
        if (tasks.size() < maxTaskSize) {
            tasks.add(task);
        } else {
            System.out.println("reject policy igniting");
            rejectPolicy.run();
        }
    }


    public void shutdown(){
        this.shutdown = true;
    }

    public static Runnable REJECT = new Runnable() {
        @Override
        public void run() {
            throw new RuntimeException("tasks queue is full");
        }
    };

    public  static Runnable DROP = new Runnable() {
        @Override
        public void run() {
            System.out.println("tasks queue is full, dropping new task!");
        }
    };

}
