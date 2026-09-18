import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongSum {

    public static void main(String[] args) throws IOException, InterruptedException {

        Runtime runtime = Runtime.getRuntime();

        System.out.println("=== SYSTEM INFO ===");

        System.out.println("Available processors: "
                + runtime.availableProcessors());

        System.out.println("Max memory (MB): "
                + runtime.maxMemory() / 1024 / 1024);

        System.out.println("Total memory (MB): "
                + runtime.totalMemory() / 1024 / 1024);

        System.out.println("Free memory (MB): "
                + runtime.freeMemory() / 1024 / 1024);

        System.out.println("Java version: "
                + System.getProperty("java.version"));

        Path dir = Path.of(System.getProperty("user.dir")).resolve("output").toAbsolutePath();
        Long begin = System.currentTimeMillis();

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        System.out.println(" Starting ...");
        AtomicLong total = new AtomicLong(0L);

        try (Stream<Path> files = Files.list(dir)) {

            files.filter(Files::isRegularFile)
                    .forEach(file ->
                            pool.submit(() -> {
                                long fileSum = sumFile(file);
                                total.addAndGet(fileSum);
                            })
                    );
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.MINUTES);

        System.out.println("TOTAL = " + total.get());
        System.out.println("Took: " + (System.currentTimeMillis() - begin) / 1000f + " seconds");
    }

    private static long sumFile(Path file) {
        long sum = 0L;
        try {
            List<String> files = Files.readAllLines(file);
            for (int i = 0; i < files.size(); i++) {
                for (String p : files.get(i).split(",")) {
                    sum += calculateCoefficient(Long.parseLong(p), 500);
                }
                if (i % 5000 == 0) {
                    System.out.println("5000 files been processed");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sum;
    }

    public static long calculateCoefficient(long input, int iterations) {
        LongStream.range(0, iterations).forEach(i -> {
            double x = input + System.nanoTime();
            Math.sqrt(Math.sqrt(
                    (Math.cos(Math.sin(Math.log(Math.sqrt(x + 1.0)))) *
                            Math.sqrt(Math.cos(Math.sin(Math.log(Math.sqrt(x + 2.0))))))));
        });

        double x = input + System.nanoTime();
        return (long) Math.sqrt(10 * Math.sqrt(
                (Math.cos(Math.sin(Math.log(Math.sqrt(x + 1.0)))) *
                        Math.sqrt(Math.cos(Math.sin(Math.log(Math.sqrt(x + 2.0))))))));
    }



}