import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }

        while(true){
                Thread.sleep(300);
            Map<Thread, StackTraceElement[]> all = Thread.getAllStackTraces();
            long matched = all.keySet().stream()
                    .filter(t -> t.getName().contains("ForkJoinPool"))
                    .count();
            System.out.println("=== " + matched + " ForkJoinPool threads @ " + java.time.LocalTime.now() + " ===");
            for (Thread t : all.keySet()) {
                if (!t.getName().contains("ForkJoinPool")) continue;
                System.out.printf("[%s] name=%s state=%s daemon=%s%n",
                        t.isVirtual() ? "virtual" : "platform",
                        t.getName(), t.getState(), t.isDaemon());
            }
        }
    }
}
