import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ThreadLocalRandom;

public class FileGenerator {

    private static final int NUMBERS_PER_LINE = 10;

    public static void main(String[] args) throws IOException {
        int N = 10000;
        int M = 10;

        Path outputDir = Path.of("output");
        Files.createDirectories(outputDir);

        for (int i = 1; i <= N; i++) {
            Path filePath = outputDir.resolve(i + ".txt");
            generateFile(filePath, M);
        }

        System.out.println("Done. Generated files in: " + outputDir.toAbsolutePath());
    }

    private static void generateFile(Path filePath, int lines) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (int i = 0; i < lines; i++) {
                writer.write(generateLine());
                writer.newLine();
            }
        }
    }

    private static String generateLine() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(700);

        for (int i = 0; i < NUMBERS_PER_LINE; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(rnd.nextInt(Integer.MAX_VALUE));
        }

        return sb.toString();
    }
}