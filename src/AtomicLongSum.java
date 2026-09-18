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


        Path dir = Path.of(System.getProperty("user.dir")).resolve("output").toAbsolutePath();
        Long begin = System.currentTimeMillis();

        ExecutorService ioPool = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService cpuBoundPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        System.out.println(" Starting ...");
        AtomicLong total = new AtomicLong(0L);

        try (Stream<Path> files = Files.list(dir)) {
            files.filter(Files::isRegularFile)
                    .forEach(file ->
                            ioPool.submit(() -> {
                                try {
                                    List<String> lines = Files.readAllLines(file);
                                    cpuBoundPool.submit(() -> {
                                        long fileSum = sumFile(lines);
                                        total.addAndGet(fileSum);
                                    });
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                    );
        }

        ioPool.shutdown();
        ioPool.awaitTermination(10, TimeUnit.MINUTES);

        cpuBoundPool.shutdown();
        cpuBoundPool.awaitTermination(10, TimeUnit.MINUTES);

        System.out.println("TOTAL = " + total.get());
        System.out.println("Took: " + (System.currentTimeMillis() - begin) / 1000f + " seconds");
    }

    private static long sumFile(List<String> lines) {
        long sum = 0L;
        try {
            for (int i = 0; i < lines.size(); i++) {
                for (String p : lines.get(i).split(",")) {
                    sum += calculateCoefficient(Long.parseLong(p), 500);
                }
            }
        } catch (Exception e) {
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